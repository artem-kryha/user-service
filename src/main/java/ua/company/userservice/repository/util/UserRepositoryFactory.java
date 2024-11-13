package ua.company.userservice.repository.util;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ua.company.userservice.configuration.properties.DataSourceProperties;
import ua.company.userservice.configuration.properties.DataSourceProperty;
import ua.company.userservice.repository.UserRepository;
import ua.company.userservice.repository.impl.JdbcUserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserRepositoryFactory {
    private final Map<String, UserRepository> repositories;

    public UserRepositoryFactory(Map<String, JdbcTemplate> jdbcTemplates, DataSourceProperties dataSourceProperties) {
        this.repositories = dataSourceProperties.getDataSources().stream()
                .collect(Collectors.toMap(
                        DataSourceProperty::getName,
                        dataSource -> new JdbcUserRepository(
                                jdbcTemplates.get(dataSource.getName()),
                                dataSource.getTable(),
                                dataSource.getMapping()
                        )
                ));
    }

    public List<UserRepository> getAllRepositories() {
        return new ArrayList<>(repositories.values());
    }
}
