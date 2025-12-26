package harshal.harshal.utility;

import harshal.harshal.configuration.ShardPropertyConfiguration;
import org.springframework.stereotype.Component;

import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

@Component
public class ConsistentHashShardResolver {

    private static final int VIRTUAL_NODES = 100;

    private final NavigableMap<Long, Integer> ring = new TreeMap<>();
    private final ShardPropertyConfiguration shardPropertyConfiguration;

    public ConsistentHashShardResolver(ShardPropertyConfiguration shardPropertyConfiguration) {
        this.shardPropertyConfiguration = shardPropertyConfiguration;
        initRing();
    }

    private void initRing() {
        for (int shardId = 0; shardId < shardPropertyConfiguration.count(); shardId++) {
            addShard(shardId);
        }
    }

    private void addShard(int shardId) {
        for (int v = 0; v < VIRTUAL_NODES; v++) {
            long hash = hash("shard-" + shardId + "-vn-" + v);
            ring.put(hash, shardId);
        }
    }

    public int resolveShard(UUID id) {
        long hash = hash(id.toString());
        var entry = ring.ceilingEntry(hash);
        return entry != null
                ? entry.getValue()
                : ring.firstEntry().getValue();
    }

    private long hash(String key) {
        return key.hashCode() & 0xffffffffL;
    }
}
