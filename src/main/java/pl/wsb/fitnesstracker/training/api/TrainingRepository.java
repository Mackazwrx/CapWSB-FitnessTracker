package pl.wsb.fitnesstracker.training.api;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wsb.fitnesstracker.user.api.User;

import java.util.Date;
import java.util.List;

public interface TrainingRepository extends JpaRepository<Training, Long> {

    List<Training> findByUser(User user);
    
    List<Training> findByUserId(Long userId);

    List<Training> findByStartTimeBetween(Date startTime, Date endTime);
}
