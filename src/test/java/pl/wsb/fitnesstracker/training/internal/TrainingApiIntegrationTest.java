package pl.wsb.fitnesstracker.training.internal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import pl.wsb.fitnesstracker.IntegrationTest;
import pl.wsb.fitnesstracker.IntegrationTestBase;
import pl.wsb.fitnesstracker.training.api.Training;
import pl.wsb.fitnesstracker.user.api.User;

import java.time.LocalDate;
import java.util.Date;

import static java.util.UUID.randomUUID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@Transactional
@AutoConfigureMockMvc(addFilters = false)
class TrainingApiIntegrationTest extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnAllTrainings_whenGettingAllTrainings() throws Exception {
        User user = existingUser(generateUser());
        Training training1 = persistTraining(generateTraining(user));
        Training training2 = persistTraining(generateTraining(user));

        mockMvc.perform(get("/v1/trainings").contentType(MediaType.APPLICATION_JSON))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(training1.getId()))
                .andExpect(jsonPath("$[1].id").value(training2.getId()));
    }

    @Test
    void shouldReturnTrainingsForUser_whenGettingTrainingsByUserId() throws Exception {
        User user1 = existingUser(generateUser());
        User user2 = existingUser(generateUser());
        Training training1 = persistTraining(generateTraining(user1));
        Training training2 = persistTraining(generateTraining(user2));

        mockMvc.perform(get("/v1/trainings/user/{userId}", user1.getId()).contentType(MediaType.APPLICATION_JSON))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(training1.getId()))
                .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(1)));
    }

    private User generateUser() {
        return new User(randomUUID().toString(), randomUUID().toString(), LocalDate.now(), randomUUID().toString());
    }
    
    private Training generateTraining(User user) {
        return new Training(
                user,
                new Date(),
                new Date(),
                ActivityType.RUNNING,
                10.0,
                5.0
        );
    }
}
