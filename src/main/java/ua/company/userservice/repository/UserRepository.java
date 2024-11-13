package ua.company.userservice.repository;

import ua.company.userservice.dto.UserDto;

import java.util.List;
import java.util.Map;

public interface UserRepository {
    List<UserDto> findUsersWithFilters(Map<String, String> filters);
}
