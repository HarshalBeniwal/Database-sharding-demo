package harshal.harshal.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(ShardPropertyConfiguration.class)
public class ShardDataSourceConfig {

    @Bean
    public Map<Integer, DataSource> shardDataSources(ShardPropertyConfiguration props) {

        Map<Integer, DataSource> dataSources = new HashMap<>();

        props.datasources().forEach((shardName, dsProps) -> {
            int shardId = Integer.parseInt(shardName.replace("shard", ""));

            DataSource ds = dsProps
                    .initializeDataSourceBuilder()
                    .build();

            dataSources.put(shardId, ds);
        });

        return dataSources;
    }

    @Bean
    public Map<Integer, NamedParameterJdbcTemplate> shardJdbcTemplates(
            Map<Integer, DataSource> shardDataSources) {

        Map<Integer, NamedParameterJdbcTemplate> templates = new HashMap<>();

        shardDataSources.forEach((shardId, ds) ->
                templates.put(shardId, new NamedParameterJdbcTemplate(ds))
        );

        return templates;
    }
}

