package ua.company.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.company.userservice.dto.UserDto;
import ua.company.userservice.repository.util.UserRepositoryFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepositoryFactory repositoryFactory;

    public List<UserDto> getUsersWithFilters(String username, String firstName, String lastName) {
        Map<String, String> filters = new HashMap<>();
        Optional.ofNullable(username).ifPresent(value -> filters.put("username", value));
        Optional.ofNullable(firstName).ifPresent(value -> filters.put("name", value));
        Optional.ofNullable(lastName).ifPresent(value -> filters.put("surname", value));

        return repositoryFactory.getAllRepositories().stream()
                .flatMap(repository -> repository.findUsersWithFilters(filters).stream())
                .toList();
    }
}
