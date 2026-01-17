package pl.wsb.fitnesstracker.training.internal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.wsb.fitnesstracker.training.api.Training;
import pl.wsb.fitnesstracker.training.api.TrainingRepository;
import pl.wsb.fitnesstracker.user.api.User;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
class WeeklyTrainingReportJob {

    private final TrainingRepository trainingRepository;

    @Scheduled(cron = "0 0 8 * * MON")
    void generateWeeklyTrainingReport() {
        LocalDate today = LocalDate.now();
        LocalDate thisMonday = today.with(DayOfWeek.MONDAY);
        LocalDate lastMonday = thisMonday.minusWeeks(1);

        LocalDateTime from = lastMonday.atStartOfDay();
        LocalDateTime to = thisMonday.atStartOfDay();

        Date fromDate = Date.from(from.atZone(ZoneId.systemDefault()).toInstant());
        Date toDate = Date.from(to.atZone(ZoneId.systemDefault()).toInstant());

        List<Training> trainings = trainingRepository.findByStartTimeBetween(fromDate, toDate);

        if (trainings.isEmpty()) {
            log.info("Weekly training report: no trainings between {} and {}", fromDate, toDate);
            return;
        }

        Map<User, List<Training>> trainingsByUser = trainings.stream()
                .collect(Collectors.groupingBy(Training::getUser));

        log.info("Weekly training report for period {} - {}", fromDate, toDate);

        trainingsByUser.forEach((user, userTrainings) -> {
            int totalTrainings = userTrainings.size();
            double totalDistance = userTrainings.stream()
                    .mapToDouble(Training::getDistance)
                    .sum();
            double averageSpeed = userTrainings.stream()
                    .mapToDouble(Training::getAverageSpeed)
                    .average()
                    .orElse(0.0);

            log.info(
                    "User: {} {} ({}) - trainings: {}, total distance: {}, average speed: {}",
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    totalTrainings,
                    totalDistance,
                    averageSpeed
            );
        });
    }
}

