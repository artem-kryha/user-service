package ua.company.userservice.repository.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import ua.company.userservice.dto.UserDto;
import ua.company.userservice.repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JdbcUserRepository implements UserRepository {
    private final JdbcTemplate jdbcTemplate;
    private final String table;
    private final Map<String, String> mapping;

    public JdbcUserRepository(JdbcTemplate jdbcTemplate, String table, Map<String, String> mapping) {
        this.jdbcTemplate = jdbcTemplate;
        this.table = table;
        this.mapping = mapping;
    }

    @Override
    public List<UserDto> findUsersWithFilters(Map<String, String> filters) {
        StringBuilder queryBuilder = new StringBuilder(String.format(
                "SELECT %s AS id, %s AS username, %s AS name, %s AS surname FROM %s",
                mapping.get("id"), mapping.get("username"), mapping.get("name"), mapping.get("surname"),
                table
        ));

        if (!filters.isEmpty()) {
            queryBuilder.append(" WHERE ");
            queryBuilder.append(filters.keySet().stream()
                    .map(s -> String.format("%s = ?", mapping.get(s)))
                    .collect(Collectors.joining(" AND "))
            );
        }

        Object[] params = filters.values().toArray();

        return jdbcTemplate.query(queryBuilder.toString(), params, (rs, rowNum) -> new UserDto(
                rs.getString("id"),
                rs.getString("username"),
                rs.getString("name"),
                rs.getString("surname")
        ));
    }
}
