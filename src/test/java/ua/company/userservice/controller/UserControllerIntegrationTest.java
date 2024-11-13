package ua.company.userservice.controller;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import ua.company.userservice.configuration.AbstractIntegrationTest;
import ua.company.userservice.dto.UserDto;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class UserControllerIntegrationTest extends AbstractIntegrationTest {

    public static final String HOST = "http://localhost:";
    public static final String USERS_PATH = "/users";
    private final RestTemplate restTemplate = new RestTemplate();

    @ParameterizedTest
    @MethodSource("filterTestCases")
    void shouldFilterUsers(String queryParams, int expectedSize, List<String> expectedUsernames) {
        String baseUrl = HOST + port;
        String url = baseUrl + USERS_PATH + queryParams;

        ResponseEntity<List<UserDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        List<UserDto> users = response.getBody();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(users).hasSize(expectedSize);
        assertThat(users).extracting(UserDto::getUsername).containsExactlyInAnyOrderElementsOf(expectedUsernames);
    }

    static Stream<Object[]> filterTestCases() {
        return Stream.of(
                new Object[]{"?username=user1", 1, List.of("user1")},
                new Object[]{"?firstName=FirstName2&lastName=LastName2", 1, List.of("user2")},
                new Object[]{"", 2, List.of("user1", "user2")}
        );
    }
}

