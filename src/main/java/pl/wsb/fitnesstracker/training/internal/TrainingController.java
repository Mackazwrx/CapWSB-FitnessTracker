package pl.wsb.fitnesstracker.training.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.wsb.fitnesstracker.training.api.TrainingDto;
import pl.wsb.fitnesstracker.training.api.TrainingService;
import pl.wsb.fitnesstracker.user.api.User;
import pl.wsb.fitnesstracker.user.api.UserNotFoundException;
import pl.wsb.fitnesstracker.user.api.UserProvider;

import java.util.List;

/**
 * REST Controller for managing trainings.
 * Provides endpoints for retrieving and creating trainings.
 */
@RestController
@RequestMapping("/v1/trainings")
@RequiredArgsConstructor
public class TrainingController {

    private final TrainingService trainingService;
    private final UserProvider userProvider;
    private final TrainingMapper trainingMapper;

    /**
     * Retrieves all trainings.
     *
     * @return A list of {@link TrainingDto} representing all trainings
     */
    @GetMapping
    public List<TrainingDto> getAllTrainings() {
        return trainingService.getAllTrainings().stream()
                .map(trainingMapper::toDto)
                .toList();
    }

    /**
     * Retrieves trainings for a specific user.
     *
     * @param userId The ID of the user
     * @return A list of {@link TrainingDto} for the specified user
     */
    @GetMapping("/user/{userId}")
    public List<TrainingDto> getTrainingsByUser(@PathVariable Long userId) {
        return trainingService.getTrainingsByUserId(userId).stream()
                .map(trainingMapper::toDto)
                .toList();
    }
    
    /**
     * Creates a new training.
     *
     * @param trainingDto The training data transfer object
     * @return The created {@link TrainingDto}
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TrainingDto createTraining(@RequestBody TrainingDto trainingDto) {
        if (trainingDto.getUser() == null || trainingDto.getUser().id() == null) {
            throw new IllegalArgumentException("User ID must be provided");
        }
        
        User user = userProvider.getUser(trainingDto.getUser().id())
                .orElseThrow(() -> new UserNotFoundException(trainingDto.getUser().id()));
                
        return trainingMapper.toDto(trainingService.createTraining(
                trainingMapper.toEntity(trainingDto, user)
        ));
    }
}
