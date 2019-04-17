package naakcii.by.api.subscriber;

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
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
public class SubscriberControllerIntegrationTest {

    private static final Logger logger = LogManager.getLogger(SubscriberControllerIntegrationTest.class);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestEntityManager testEntityManager;

    private ObjectMapper objectMapper;
    private StopWatch stopWatch;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        stopWatch = new StopWatch();
    }

    @Test
    public void test_given_subscriberDto_then_return_json() throws Exception {
        SubscriberDTO subscriberDTO = new SubscriberDTO();
        subscriberDTO.setEmail("email@email.com");
        String postJson = objectMapper.writeValueAsString(subscriberDTO);
        subscriberDTO.setId(1L);
        String expectedJson = objectMapper.writeValueAsString(subscriberDTO);
        logger.info("Starting of request '{}({})' execution.", "POST", "/subscribers");
        stopWatch.start();
        MvcResult mvcResult = this.mockMvc.perform(post("/subscribers")
                .content(postJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(ApiConfigConstants.API_V_2_0))
                .andExpect(status().isOk())
                .andExpect(content().encoding(StandardCharsets.UTF_8.name()))
                .andExpect(content().contentTypeCompatibleWith(ApiConfigConstants.API_V_2_0))
                .andDo(print())
                .andReturn();
        stopWatch.stop();
        logger.info("Execution of request '{}({})' has finished.", "POST", "/subscribers");
        logger.info("Execution time is: {} milliseconds.", stopWatch.getTotalTimeMillis());
        String resultJson = mvcResult.getResponse().getContentAsString();
        assertEquals("Expected json should be: {"
                + "\"id\":1,"
                + "\"email\":\"email@email.com\","
                + "}.", expectedJson, resultJson);

        logger.info("Removing of test data.");
        try {
            Subscriber subscriberFromDb = testEntityManager.find(Subscriber.class, 1L);
            testEntityManager.remove(subscriberFromDb);
            testEntityManager.flush();
            logger.info("Test data was cleaned successfully: instances of '{}' were removed from the database.",
                    Subscriber.class);
        } catch (Exception exception) {
            logger.error("Exception has occured during the cleaning of test data ('{}' instances): {}.",
                    Subscriber.class, exception);
        }
    }
}
