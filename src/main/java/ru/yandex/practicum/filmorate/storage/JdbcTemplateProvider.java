package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

@Component
public class JdbcTemplateProvider {
    public static final String JDBC_URL = "jdbc:h2:file:./db/filmorate";
    public static final String JDBC_USERNAME = "sa";
    public static final String JDBC_PASSWORD = "password";
    public static final String JDBC_DRIVER = "org.h2.Driver";

    public JdbcTemplate getJdbcTemplate() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(JDBC_DRIVER);
        dataSource.setUrl(JDBC_URL);
        dataSource.setUsername(JDBC_USERNAME);
        dataSource.setPassword(JDBC_PASSWORD);
        return new JdbcTemplate(dataSource);
    }
}
