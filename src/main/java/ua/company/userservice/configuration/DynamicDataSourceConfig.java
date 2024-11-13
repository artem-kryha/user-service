package ua.company.userservice.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import ua.company.userservice.configuration.properties.DataSourceProperties;
import ua.company.userservice.configuration.properties.DataSourceProperty;

import javax.sql.DataSource;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@RequiredArgsConstructor
public class DynamicDataSourceConfig {

    private final DataSourceProperties dataSourceProps;

    @Bean
    public Map<String, JdbcTemplate> jdbcTemplates() {
        return dataSourceProps.getDataSources().stream()
                .collect(Collectors.toMap(
                        DataSourceProperty::getName,
                        props -> new JdbcTemplate(createDataSource(props))
                ));
    }

    private DataSource createDataSource(DataSourceProperty property) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(property.getDriver());
        dataSource.setUrl(property.getUrl());
        dataSource.setUsername(property.getUser());
        dataSource.setPassword(property.getPassword());
        return dataSource;
    }
}
