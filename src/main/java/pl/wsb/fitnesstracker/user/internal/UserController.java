package pl.wsb.fitnesstracker.user.internal;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.wsb.fitnesstracker.user.api.UserDto;
import pl.wsb.fitnesstracker.user.api.UserEmailDto;
import pl.wsb.fitnesstracker.user.api.UserSimpleDto;
import pl.wsb.fitnesstracker.user.api.UserNotFoundException;

import java.util.List;

/**
 * UserController is responsible for handling HTTP requests related to user operations.
 * It provides endpoints for retrieving, creating, updating, and deleting users.
 */
@RestController
@RequestMapping("/v1/users")
class UserController {

    private final UserServiceImpl userService;

    private final UserMapper userMapper;

    public UserController(UserServiceImpl userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    /**
     * Retrieves all users.
     *
     * @return A list of {@link UserSimpleDto} representing all users
     */
    @GetMapping
    public List<UserSimpleDto> getAllUsers() {
        return userService.findAllUsers()
                .stream()
                .map(userMapper::toSimpleDto)
                .toList();
    }

    /**
     * Retrieves a user by ID.
     *
     * @param id The ID of the user to retrieve
     * @return The {@link UserDto} of the found user
     */
    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id) {
        return userService.getUser(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    /**
     * Creates a new user.
     *
     * @param userDto The DTO containing the user data
     * @return The created {@link UserDto}
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody UserDto userDto) {
        return userMapper.toDto(userService.createUser(userMapper.toEntity(userDto)));
    }

    /**
     * Deletes a user by ID.
     *
     * @param id The ID of the user to delete
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    /**
     * Updates a user.
     *
     * @param id      The ID of the user to update
     * @param userDto The DTO containing the updated user data
     * @return The updated {@link UserDto}
     */
    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        // Since we are updating, we need to make sure the ID is correct
        // For simplicity, we can fetch the user, update fields, and save.
        // Or we can use the mapper to create an entity and set the ID.
        // However, UserMapper.toEntity creates a new User without ID.
        // We should probably handle this carefully.
        
        // Strategy: Retrieve existing user, update fields from DTO, save.
        // But the requirement says "update user (arbitrarily selected attribute)".
        // And UserService has updateUser(User).
        
        // Let's use the provided UserService.updateUser.
        // We need to convert DTO to Entity.
        // But we need the ID.
        
        // Let's fetch the existing user first to ensure it exists.
        pl.wsb.fitnesstracker.user.api.User existingUser = userService.getUser(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        
        // Update fields
        if (userDto.firstName() != null) existingUser.setFirstName(userDto.firstName());
        if (userDto.lastName() != null) existingUser.setLastName(userDto.lastName());
        if (userDto.birthdate() != null) existingUser.setBirthdate(userDto.birthdate());
        if (userDto.email() != null) existingUser.setEmail(userDto.email());
        
        return userMapper.toDto(userService.updateUser(existingUser));
    }

    /**
     * Searches users by email fragment.
     *
     * @param email The email fragment to search for
     * @return A list of {@link UserEmailDto} matching the criteria
     */
    @GetMapping("/search/email")
    public List<UserEmailDto> searchUsersByEmail(@RequestParam String email) {
        return userService.searchUsersByEmail(email)
                .stream()
                .map(userMapper::toEmailDto)
                .toList();
    }

    /**
     * Searches users older than the specified age.
     *
     * @param age The age threshold
     * @return A list of {@link UserDto} matching the criteria
     */
    @GetMapping("/search/age")
    public List<UserDto> searchUsersByAge(@RequestParam int age) {
        return userService.searchUsersByAge(age)
                .stream()
                .map(userMapper::toDto)
                .toList();
    }
}

