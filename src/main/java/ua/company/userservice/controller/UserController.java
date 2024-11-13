package ua.company.userservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.company.userservice.dto.UserDto;
import ua.company.userservice.service.UserService;

import java.util.List;

@RestController
@Tag(name = "Users", description = "Endpoints for managing and retrieving user information")
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(
            summary = "Retrieve users",
            description = "Get all users or filter users by specific parameters such as username, first name, last name, or ID."
    )
    public List<UserDto> getAllUsers(
            @Parameter(
                    description = "Filter by username",
                    schema = @Schema(type = "string", example = "johndoe")
            )
            @RequestParam(required = false) String username,

            @Parameter(
                    description = "Filter by last name",
                    schema = @Schema(type = "string", example = "Doe")
            )
            @RequestParam(required = false) String lastName,

            @Parameter(
                    description = "Filter by first name",
                    schema = @Schema(type = "string", example = "John")
            )
            @RequestParam(required = false) String firstName
    ) {
        return userService.getUsersWithFilters(username, firstName, lastName);
    }
}
