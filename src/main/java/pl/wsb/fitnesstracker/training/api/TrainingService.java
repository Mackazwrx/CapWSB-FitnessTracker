package pl.wsb.fitnesstracker.training.api;

import java.util.List;

/**
 * Interface (API) for managing Training entities.
 */
public interface TrainingService {

    /**
     * Retrieves all trainings available in the system.
     *
     * @return A list of all {@link Training} entities
     */
    List<Training> getAllTrainings();

    /**
     * Retrieves all trainings belonging to a specific user.
     *
     * @param userId The ID of the user
     * @return A list of {@link Training} entities for the specified user
     */
    List<Training> getTrainingsByUserId(Long userId);
    
    /**
     * Creates a new training in the system.
     *
     * @param training The training entity to be created
     * @return The created {@link Training} entity
     */
    Training createTraining(Training training);
}
