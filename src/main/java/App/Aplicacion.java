package App;

import java.util.logging.Logger;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.sqlite.SQLiteDataSource;

@SpringBootApplication
public class Aplicacion {

    private static final Logger log = Logger.getLogger(Aplicacion.class.getName());

    public static void main(String[] args) throws Exception {
        new SpringApplicationBuilder(Aplicacion.class).run(args);
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.sqlite.JDBC");
        dataSourceBuilder.url("jdbc:sqlite://sqlite/db/proyecto.db");
        dataSourceBuilder.type(SQLiteDataSource.class);
        return dataSourceBuilder.build();
    }
}
