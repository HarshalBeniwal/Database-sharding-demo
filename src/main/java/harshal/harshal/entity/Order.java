package harshal.harshal.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table( name = "order")
public class Order {

    private UUID orderId;
    // For learning kept it easy  , but in distributed world , if we were to
    // keep this as long , then a distributed id generator needs to be implemented.


    private Long userId;

    private BigDecimal amount;

    private Instant createdAt;
}
