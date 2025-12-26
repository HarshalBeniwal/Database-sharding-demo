package harshal.harshal.repository;

import harshal.harshal.entity.Order;
import harshal.harshal.utility.ConsistentHashShardResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class ShardedOrderRepository {

    private final Map<Integer, NamedParameterJdbcTemplate> shardTemplates;
    private final ConsistentHashShardResolver shardResolver;

    // ---------------- WRITE ----------------

    public void save(Order order) {
        int shardId = shardResolver.resolveShard(order.getOrderId());
        NamedParameterJdbcTemplate jdbc = shardTemplates.get(shardId);

        String sql = """
                    INSERT INTO orders (order_id, user_id, amount, created_at)
                    VALUES (:orderId, :userId, :amount, :createdAt)
                """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("orderId", order.getOrderId())
                .addValue("userId", order.getUserId())
                .addValue("amount", order.getAmount())
                .addValue("createdAt", Timestamp.from(order.getCreatedAt()));

        jdbc.update(sql, params);
    }

    // ---------------- READ (single shard) ----------------

    public Optional<Order> findById(UUID orderId) {
        int shardId = shardResolver.resolveShard(orderId);
        NamedParameterJdbcTemplate jdbc = shardTemplates.get(shardId);

        List<Order> results = jdbc.query(
                "SELECT order_id, user_id, amount, created_at FROM orders WHERE order_id = :orderId",
                new MapSqlParameterSource("orderId", orderId),
                this::mapRow
        );

        return results.stream().findFirst();
    }

    // ---------------- READ (cross-shard fan-out) ----------------

    public List<Order> getHighValueOrders(BigDecimal amount) {
        List<Order> orders = new ArrayList<>();

        String sql = """
                    SELECT order_id, user_id, amount, created_at
                    FROM orders
                    WHERE amount >= :amount
                """;

        MapSqlParameterSource params =
                new MapSqlParameterSource("amount", amount);

        for (NamedParameterJdbcTemplate jdbc : shardTemplates.values()) {
            List<Order> shardOrders = jdbc.query(sql, params, this::mapRow);
            orders.addAll(shardOrders);
        }

        return orders;
    }


    private Order mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Order.builder()
                .orderId(rs.getObject("order_id", UUID.class))
                .userId(rs.getLong("user_id"))
                .amount(rs.getBigDecimal("amount"))
                .createdAt(rs.getTimestamp("created_at").toInstant())
                .build();
    }


}
