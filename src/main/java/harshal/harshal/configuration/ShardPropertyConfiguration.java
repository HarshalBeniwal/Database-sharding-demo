package harshal.harshal.configuration;

import jakarta.validation.Valid;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "app.shards")
public record ShardPropertyConfiguration(
        int count,
        Map<String, DataSourceProperties> datasources
) {}
