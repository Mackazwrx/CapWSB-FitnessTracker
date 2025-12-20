package pl.wsb.fitnesstracker.user.internal;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wsb.fitnesstracker.user.api.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Query searching users by email address. It matches by exact match.
     *
     * @param email email of the user to search
     * @return {@link Optional} containing found user or {@link Optional#empty()} if none matched
     */
    default Optional<User> findByEmail(String email) {
        return findAll().stream()
                .filter(user -> Objects.equals(user.getEmail(), email))
                .findFirst();
    }

    /**
     * Query searching users by email address. It matches by partial match and is case-insensitive.
     *
     * @param email email fragment of the user to search
     * @return List of found users
     */
    default List<User> findByEmailContainingIgnoreCase(String email) {
        String lowerCaseEmail = email.toLowerCase();
        return findAll().stream()
                .filter(user -> user.getEmail() != null && user.getEmail().toLowerCase().contains(lowerCaseEmail))
                .collect(Collectors.toList());
    }

    /**
     * Query searching users older than specified age.
     *
     * @param age age threshold
     * @return List of found users
     */
    default List<User> findUsersOlderThan(int age) {
        LocalDate cutoffDate = LocalDate.now().minusYears(age);
        return findAll().stream()
                .filter(user -> user.getBirthdate() != null && user.getBirthdate().isBefore(cutoffDate))
                .collect(Collectors.toList());
    }

}
