package com.gique.todo;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;


@SpringBootApplication
@EnableAutoConfiguration
@Controller
public class Application {
    private static final Logger log = LogManager.getLogger(Application.class);

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.hikari.maximum-pool-size}")
    private int maximumPoolSize;

    @Value("${spring.datasource.hikari.minimum-idle}")
    private int minimumIdle;

    @Value("${spring.datasource.hikari.leak-detection-threshold}")
    private int leakDetectionThreshold;

    @Value("${spring.datasource.hikari.idle-timeout}")
    private int idleTimeout;

    @Value("${spring.datasource.hikari.connection-timeout}")
    private int connectionTimeout;

    @Value("${spring.datasource.hikari.validation-timeout}")
    private int validationTimeout;

    @Autowired
    private DataSource dataSource;

    @Bean
    public DataSource dataSource() throws SQLException {
        if (dbUrl == null || dbUrl.isEmpty()) {
            return new HikariDataSource();
        } else {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(dbUrl);
            config.setMaximumPoolSize(maximumPoolSize);
            config.setMinimumIdle(minimumIdle);
            config.setLeakDetectionThreshold(leakDetectionThreshold);
            config.setIdleTimeout(idleTimeout);
            config.setConnectionTimeout(connectionTimeout);
            config.setValidationTimeout(validationTimeout);
            return new HikariDataSource(config);
        }
    }

    @PostConstruct
    public void init() {
        log.warn("Memory: Max[{}], Free[{}], Total[{}]", Runtime.getRuntime().maxMemory(),
                Runtime.getRuntime().freeMemory(), Runtime.getRuntime().totalMemory());

        try (Connection connection = dataSource.getConnection()) {
            Statement stmt = dataSource.getConnection().createStatement();
            //stmt.executeUpdate("DROP TABLE IF EXISTS todo");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS todo (id SERIAL PRIMARY KEY, line_id TEXT NOT NULL,  task TEXT NOT NULL, status  char(10), important char(1), due_date TIMESTAMP NOT NULL, created_at TIMESTAMP NOT NULL, updated_at TIMESTAMP NOT NULL);");
            stmt.executeUpdate("INSERT INTO todo (line_id, task, status, important, due_date, created_at, updated_at) VALUES ('id_1_test', 'test task', 'incomplete', '0', now(), now(), now());");
            stmt.executeUpdate("INSERT INTO todo (line_id, task, status, important, due_date, created_at, updated_at) VALUES ('id_1_test', 'test task 2', 'completed', '0', now(), now(), now());");
            //stmt.executeUpdate("INSERT INTO todo (line_id, task, status, important, due_date, created_at, updated_at) VALUES ('id_1_test', 'test task 3', 'overdue', '0', now(), now(), now());");
            ResultSet rs = stmt.executeQuery("SELECT * FROM todo WHERE line_id = 'id_1_test'");
            while (rs.next()) {
                System.out.println("Read from DB: " + rs.getString("task"));
            }
            if(stmt != null){ stmt.close(); connection.close(); }
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        }

    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
