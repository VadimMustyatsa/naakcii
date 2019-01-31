package naakcii.by.api.subcategory;

import com.fasterxml.jackson.databind.ObjectMapper;
import naakcii.by.api.APIApplication;
import naakcii.by.api.category.Category;
import naakcii.by.api.config.ApiConfigConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
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
public class SubcategoryControllerIntegrationTest {

    private static final Logger logger = LogManager.getLogger(SubcategoryControllerIntegrationTest.class);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestEntityManager testEntityManager;

    private ObjectMapper objectMapper;
    private StopWatch stopWatch;
    private List<Category> categories;
    private Subcategory firstSubcategory;
    private Subcategory secondSubcategory;
    private Subcategory thirdSubcategory;
    private Subcategory fourthSubcategory;
    private Subcategory fifthSubcategory;
    private Subcategory sixthSubcategory;
    private Subcategory seventhSubcategory;
    private Long categoryId;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        stopWatch = new StopWatch();
        categories = new ArrayList<>();
    }

    private void createListOfSubcategories() {
        logger.info("Preparing of test data.");
        Category firstCategory = new Category("Молочные продукты, яйца", true);
        Category secondCategory = new Category("Хлебобулочные изделия", true);
        Category thirdCategory = new Category("Овощи и фрукты", true);
        firstCategory.setIcon("Молочные продукты, яйца.png");
        firstCategory.setPriority(1);
        secondCategory.setIcon("Хлебобулочные изделия.png");
        secondCategory.setPriority(2);
        thirdCategory.setIcon("Овощи и фрукты.png");
        thirdCategory.setPriority(3);
        firstSubcategory = new Subcategory("Кисломолочные изделия", true, firstCategory);
        firstSubcategory.setPriority(7);
        secondSubcategory = new Subcategory("Масло", true, firstCategory);
        secondSubcategory.setPriority(1);
        thirdSubcategory = new Subcategory("Сдобные изделия", true, secondCategory);
        thirdSubcategory.setPriority(5);
        fourthSubcategory = new Subcategory("Грибы", true, thirdCategory);
        fourthSubcategory.setPriority(3);
        fifthSubcategory = new Subcategory("Мороженое", true, firstCategory);
        fifthSubcategory.setPriority(9);
        sixthSubcategory = new Subcategory("Хлеб, батон", false, secondCategory);
        sixthSubcategory.setPriority(2);
        seventhSubcategory = new Subcategory("Фрукты", false, thirdCategory);
        seventhSubcategory.setPriority(4);

        try {
            categories.add(testEntityManager.persist(firstCategory));
            categories.add(testEntityManager.persist(secondCategory));
            categories.add(testEntityManager.persist(thirdCategory));
            testEntityManager.clear();
            categoryId = firstCategory.getId();
            logger.info("Test data was created successfully: instances of '{}' and '{}' were added to the database.",
                    Category.class, Subcategory.class);
        } catch (Exception exception) {
            logger.error("Exception has occurred during the creation of test data ('{}' and '{}' instances): {}.",
                    Category.class, Subcategory.class, exception);
        }
    }

    private void removeListOfSubcategories() {
        logger.info("Removing of test data.");
        try {
            categories.stream().forEach((Category category) -> testEntityManager.remove(testEntityManager.merge(category)));
            testEntityManager.flush();
            logger.info("Test data was cleaned successfully: instances of '{}' and '{}' were removed from the database.",
                    Category.class, Subcategory.class);
        } catch (Exception exception) {
            logger.error("Exception has occurred during the cleaning of test data ('{}' and '{}' instances): {}.",
                    Category.class, Subcategory.class, exception);
        }
    }

    @Test
    public void test_get_all_subcategories_by_category_id() throws Exception {
        createListOfSubcategories();
        List<SubcategoryDTO> expectedSubcategoryDTOs = new ArrayList<>();
        expectedSubcategoryDTOs.add(new SubcategoryDTO(secondSubcategory));
        expectedSubcategoryDTOs.add(new SubcategoryDTO(firstSubcategory));
        expectedSubcategoryDTOs.add(new SubcategoryDTO(fifthSubcategory));
        String expectedJson = objectMapper.writeValueAsString(expectedSubcategoryDTOs);
        logger.info("Starting of request '{}({})' execution.", "GET", "/subcategories/" + categoryId);
        logger.info("Request path variable: '{}' = '{}'.", "categoryId", categoryId);
        stopWatch.start();
        MvcResult mvcResult = this.mockMvc.perform(get("/subcategories/{categoryId}", categoryId.toString())
                .accept(ApiConfigConstants.API_V_2_0))
                .andExpect(status().isOk())
                .andExpect(content().encoding(StandardCharsets.UTF_8.name()))
                .andExpect(content().contentType("application/vnd.naakcii.api-v2.0+json;charset=UTF-8"))
                .andDo(print())
                .andReturn();
        stopWatch.stop();
        logger.info("Execution of request '{}({})' has finished.", "GET", "/subcategories/" + categoryId);
        logger.info("Execution time is: {} milliseconds.", stopWatch.getTotalTimeMillis());
        String resultJson = mvcResult.getResponse().getContentAsString();
        assertEquals("Expected JSON should be: ["
                + "{\"id\":1,\"name\":\"Масло\",\"categoryId\":1,\"priority\":1},"
                + "{\"id\":3,\"name\":\"Кисломолочные изделия\",\"categoryId\":1,\"priority\":7},"
                + "{\"id\":2,\"name\":\"Мороженое\",\"categoryId\":1,\"priority\":9}"
                + "].", expectedJson, resultJson);
        removeListOfSubcategories();
    }

    @After
    public void tearDown() {
        objectMapper = null;
        stopWatch = null;
        categories = null;
        firstSubcategory = null;
        secondSubcategory = null;
        thirdSubcategory = null;
        fourthSubcategory = null;
        fifthSubcategory = null;
        categoryId = null;
    }
}
