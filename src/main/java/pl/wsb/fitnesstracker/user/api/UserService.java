package pl.wsb.fitnesstracker.user.api;

import java.util.List;
import java.util.Optional;

/**
 * Interface (API) for modifying operations on {@link User} entities through the API.
 * Implementing classes are responsible for executing changes within a database transaction, whether by continuing an existing transaction or creating a new one if required.
 */
public interface UserService {

    /**
     * Creates a new user.
     *
     * @param user The user to be created
     * @return The created user
     */
    User createUser(User user);

    /**
     * Deletes a user by ID.
     *
     * @param id The ID of the user to be deleted
     */
    void deleteUser(Long id);

    /**
     * Updates a user.
     *
     * @param user The user with updated data
     * @return The updated user
     */
    User updateUser(User user);

    /**
     * Search users by email fragment (case-insensitive).
     *
     * @param email The email fragment to search for
     * @return List of matching users
     */
    List<User> searchUsersByEmail(String email);

    /**
     * Search users older than the specified age.
     *
     * @param age The age threshold
     * @return List of matching users
     */
    List<User> searchUsersByAge(int age);
}
