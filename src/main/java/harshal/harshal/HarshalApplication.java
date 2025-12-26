package harshal.harshal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jdbc.JdbcRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication(
        exclude = {
                DataSourceAutoConfiguration.class,
                JdbcRepositoriesAutoConfiguration.class,
                LiquibaseAutoConfiguration.class
        }
)
@ConfigurationPropertiesScan
public class HarshalApplication {

	public static void main(String[] args) {
		SpringApplication.run(HarshalApplication.class, args);
	}

}
