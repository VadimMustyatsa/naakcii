package naakcii.by.api.statistics;

import com.fasterxml.jackson.databind.ObjectMapper;
import naakcii.by.api.APIApplication;
import naakcii.by.api.config.ApiConfigConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = APIApplication.class)
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
@Transactional
@TestPropertySource(locations = "classpath:application-integration-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class StatisticsControllerIntegrationTest {

    private static final Logger logger = LogManager.getLogger(StatisticsControllerIntegrationTest.class);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestEntityManager testEntityManager;

    private ObjectMapper objectMapper;
    private StopWatch stopWatch;
    private List<Statistics> statistics;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        stopWatch = new StopWatch();
    }

    @Test
    public void test_return_statistics_json() throws Exception {
        createData();
        String expectedJson = objectMapper.writeValueAsString(new StatisticsDTO(statistics.get(0)));
        logger.info("Starting of request '{}({})' execution.", "GET", "/statistics");
        stopWatch.start();
        MvcResult mvcResult = this.mockMvc.perform(get("/statistics")
                .accept(ApiConfigConstants.API_V_2_0))
                .andExpect(status().isOk())
                .andExpect(content().encoding(StandardCharsets.UTF_8.name()))
                .andExpect(content().contentTypeCompatibleWith(ApiConfigConstants.API_V_2_0))
                .andDo(print())
                .andReturn();
        stopWatch.stop();
        logger.info("Execution of request '{}({})' has finished.", "GET", "/statistics");
        logger.info("Execution time is: {} ms.", stopWatch.getTotalTimeMillis());
        String resultJson = mvcResult.getResponse().getContentAsString();
        assertEquals("Expected json should be: {"
                + "\"id\":1,"
                + "\"chainQuantity\":1,"
                + "\"discountedProducts\":111,"
                + "\"averageDiscountPercentage\":11,"
                + "\"creationDateMillis\":1514754000000,"
                + "}.", expectedJson, resultJson);
        removeData();
    }

    private void createData() {
        logger.info("Preparing of test data.");
        statistics = new ArrayList<>();
        Statistics firstStatistics = new Statistics(1, 111, 11, new GregorianCalendar(2018, 0, 1));
        Statistics secondStatistics = new Statistics(1, 111, 11, new GregorianCalendar(2018, 0, 1));
        try {
            statistics.add(testEntityManager.persist(firstStatistics));
            statistics.add(testEntityManager.persist(secondStatistics));
            testEntityManager.flush();
            testEntityManager.clear();
            logger.info("Test data was created successfully: instances of '{}' were added in the database.",
                    Statistics.class);
        } catch (Exception exception) {
            logger.error("Exception has occurred during the creation of test data ('{}' instances): {}.",
                    Statistics.class, exception);
        }
    }

    private void removeData() {
        logger.info("Removing of test data.");
        try {
            statistics.stream()
                    .map(testEntityManager::merge)
                    .forEach(testEntityManager::remove);
            testEntityManager.flush();
            logger.info("Test data was cleaned successfully: instances of '{}' were removed from the database.",
                    Statistics.class);
        } catch (Exception exception) {
            logger.error("Exception has occured during the cleaning of test data ('{}' instances): {}.",
                    Statistics.class, exception);
        }
    }
}
