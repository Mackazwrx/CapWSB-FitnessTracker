package pl.wsb.fitnesstracker.training.internal;

import org.springframework.stereotype.Component;
import pl.wsb.fitnesstracker.training.api.Training;
import pl.wsb.fitnesstracker.training.api.TrainingDto;
import pl.wsb.fitnesstracker.user.api.User;
import pl.wsb.fitnesstracker.user.api.UserDto;

/**
 * Mapper for converting between {@link Training} entities and {@link TrainingDto}s.
 */
@Component
public class TrainingMapper {

    /**
     * Converts a Training entity to a TrainingDto.
     *
     * @param training The Training entity
     * @return The corresponding TrainingDto
     */
    public TrainingDto toDto(Training training) {
        if (training == null) {
            return null;
        }
        return new TrainingDto(
                training.getId(),
                toUserDto(training.getUser()),
                training.getStartTime(),
                training.getEndTime(),
                training.getActivityType(),
                training.getDistance(),
                training.getAverageSpeed()
        );
    }

    /**
     * Converts a TrainingDto to a Training entity.
     *
     * @param trainingDto The TrainingDto
     * @param user The User entity associated with the training
     * @return The created Training entity
     */
    public Training toEntity(TrainingDto trainingDto, User user) {
        if (trainingDto == null) {
            return null;
        }
        return new Training(
                user,
                trainingDto.getStartTime(),
                trainingDto.getEndTime(),
                trainingDto.getActivityType(),
                trainingDto.getDistance(),
                trainingDto.getAverageSpeed()
        );
    }
    
    /**
     * Converts a User entity to a UserDto.
     *
     * @param user The User entity
     * @return The corresponding UserDto
     */
    private UserDto toUserDto(User user) {
        if (user == null) {
            return null;
        }
        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getBirthdate(),
                user.getEmail()
        );
    }
}
