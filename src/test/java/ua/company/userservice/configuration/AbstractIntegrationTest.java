package ua.company.userservice.configuration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractIntegrationTest {

    @Autowired
    private Map<String, JdbcTemplate> jdbcTemplates;

    @LocalServerPort
    protected int port;

    private static final PostgreSQLContainer<?> POSTGRES_CONTAINER = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("users")
            .withUsername("testuser")
            .withPassword("testpass");

    private static final MySQLContainer<?> MYSQL_CONTAINER = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("users")
            .withUsername("testuser")
            .withPassword("testpass");


    @BeforeAll
    static void initialize() {
        POSTGRES_CONTAINER.start();
        MYSQL_CONTAINER.start();
    }

    @AfterAll
    static void stopContainers() {
        POSTGRES_CONTAINER.stop();
        MYSQL_CONTAINER.stop();
    }

    @BeforeEach
    void setupDatabase() {
        fillMySQLData(jdbcTemplates.get("users_mysql"));
        fillPostgresData(jdbcTemplates.get("users_postgres"));
    }

    @AfterEach
    void cleanDatabase() {
        cleanMySQLData(jdbcTemplates.get("users_mysql"));
        cleanPostgresData(jdbcTemplates.get("users_postgres"));
    }

    @DynamicPropertySource
    static void configureDynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("db.dataSources[0].name", () -> "users_postgres");
        registry.add("db.dataSources[0].driver", () -> "org.postgresql.Driver");
        registry.add("db.dataSources[0].strategy", () -> "postgres");
        registry.add("db.dataSources[0].url", POSTGRES_CONTAINER::getJdbcUrl);
        registry.add("db.dataSources[0].user", POSTGRES_CONTAINER::getUsername);
        registry.add("db.dataSources[0].password", POSTGRES_CONTAINER::getPassword);
        registry.add("db.dataSources[0].table", () -> "users");
        registry.add("db.dataSources[0].mapping.id", () -> "user_id");
        registry.add("db.dataSources[0].mapping.username", () -> "login");
        registry.add("db.dataSources[0].mapping.name", () -> "first_name");
        registry.add("db.dataSources[0].mapping.surname", () -> "last_name");

        registry.add("db.dataSources[1].name", () -> "users_mysql");
        registry.add("db.dataSources[1].driver", () -> "com.mysql.cj.jdbc.Driver");
        registry.add("db.dataSources[1].strategy", () -> "mysql");
        registry.add("db.dataSources[1].url", MYSQL_CONTAINER::getJdbcUrl);
        registry.add("db.dataSources[1].user", MYSQL_CONTAINER::getUsername);
        registry.add("db.dataSources[1].password", MYSQL_CONTAINER::getPassword);
        registry.add("db.dataSources[1].table", () -> "mysql_users");
        registry.add("db.dataSources[1].mapping.id", () -> "id");
        registry.add("db.dataSources[1].mapping.username", () -> "username");
        registry.add("db.dataSources[1].mapping.name", () -> "name");
        registry.add("db.dataSources[1].mapping.surname", () -> "surname");
    }

    private void fillMySQLData(JdbcTemplate mysqlTemplate) {
        mysqlTemplate.execute("CREATE TABLE IF NOT EXISTS mysql_users (id VARCHAR(255), username VARCHAR(255), name VARCHAR(255), surname VARCHAR(255))");
        mysqlTemplate.update("INSERT INTO mysql_users (id, username, name, surname) VALUES ('1', 'user1', 'FirstName1', 'LastName1')");
    }

    private void fillPostgresData(JdbcTemplate postgresTemplate) {
        postgresTemplate.execute("CREATE TABLE IF NOT EXISTS users (user_id VARCHAR(255), login VARCHAR(255), first_name VARCHAR(255), last_name VARCHAR(255))");
        postgresTemplate.update("INSERT INTO users (user_id, login, first_name, last_name) VALUES ('2', 'user2', 'FirstName2', 'LastName2')");
    }

    private void cleanMySQLData(JdbcTemplate mysqlTemplate) {
        mysqlTemplate.execute("DELETE FROM mysql_users");
    }

    private void cleanPostgresData(JdbcTemplate postgresTemplate) {
        postgresTemplate.execute("DELETE FROM users");
    }
}

