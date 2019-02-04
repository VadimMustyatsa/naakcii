package naakcii.by.api.category;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

import com.fasterxml.jackson.databind.ObjectMapper;

import naakcii.by.api.APIApplication;
import naakcii.by.api.config.ApiConfigConstants;
import naakcii.by.api.subcategory.Subcategory;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = APIApplication.class)
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
@Transactional
@TestPropertySource(locations = "classpath:application-integration-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CategoryControllerIntegrationTest {

	private static final Logger logger = LogManager.getLogger(CategoryControllerIntegrationTest.class);
		
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private TestEntityManager testEntityManager;
	
	private ObjectMapper objectMapper;
	private StopWatch stopWatch;
	private List<Category> activeCategories;
	private List<Category> inactiveCategories;
	
	@Before
	public void setUp() {
		objectMapper = new ObjectMapper();
		stopWatch = new StopWatch();
		activeCategories = new ArrayList<>();
		inactiveCategories = new ArrayList<>();
	}
		
	private void createListOfCategories() {
		logger.info("Preparing of test data.");
		Category firstCategory = new Category("Мясо и колбасные изделия", true);
		Category secondCategory = new Category("Молочные продукты, яйца", true);
		Category thirdCategory = new Category("Хлебобулочные изделия", true);
		Category fourthCategory = new Category("Овощи и фрукты", false);
		firstCategory.setIcon("Мясо и колбасные изделия.png");
		firstCategory.setPriority(7);
		secondCategory.setIcon("Молочные продукты, яйца.png");
		secondCategory.setPriority(2);
		thirdCategory.setIcon("Хлебобулочные изделия.png");
		thirdCategory.setPriority(4);
		fourthCategory.setIcon("Овощи и фрукты.png");
		fourthCategory.setPriority(1);
		Subcategory firstSubcategory = new Subcategory("Колбасные изделия", true, firstCategory);
		firstSubcategory.setPriority(7);
		Subcategory secondSubcategory = new Subcategory("Масло", true, secondCategory);
		secondSubcategory.setPriority(1);
		Subcategory thirdSubcategory = new Subcategory("Хлебцы", true, thirdCategory);
		thirdSubcategory.setPriority(5);
		Subcategory fourthSubcategory = new Subcategory("Грибы", true, fourthCategory);
		fourthSubcategory.setPriority(3);
				
		try {
			activeCategories.add(testEntityManager.persist(secondCategory));
			activeCategories.add(testEntityManager.persist(thirdCategory));
			activeCategories.add(testEntityManager.persist(firstCategory));
			inactiveCategories.add(testEntityManager.persist(fourthCategory));
			testEntityManager.clear();
			logger.info("Test data was created successfully: instances of '{}' and '{}' were added to the database.",
					Category.class, Subcategory.class);
		} catch (Exception exception) {
			logger.error("Exception has occurred during the creation of test data ('{}' and '{}' instances): {}.", 
					Category.class, Subcategory.class, exception);
		} 
	}
	
	private void removeListOfCategories() {	
		logger.info("Removing of test data.");
		
		try {
			activeCategories.stream()
					.map((Category category) -> testEntityManager.merge(category))
					.forEach((Category category) ->	testEntityManager.remove(category));
			inactiveCategories.stream()
					.map((Category category) -> testEntityManager.merge(category))
					.forEach((Category category) ->	testEntityManager.remove(category));
			testEntityManager.flush();
			logger.info("Test data was cleaned successfully: instances of '{}' and '{}' were removed from the database.",
					Category.class, Subcategory.class);
		} catch (Exception exception) {
			logger.error("Exception has occurred during the cleaning of test data ('{}' and '{}' instances): {}.", 
					Category.class, Subcategory.class, exception);
		}
	}
	
	@Test
	public void test_get_all_categories() throws Exception {
		createListOfCategories();
		List<CategoryDTO> expectedCategoryDTOs = activeCategories
				.stream()
				.map(CategoryDTO::new)
				.collect(Collectors.toList());
		String expectedJson = objectMapper.writeValueAsString(expectedCategoryDTOs);
		logger.info("Starting of request '{}({})' execution.", "GET", "/categories");
		stopWatch.start();
		MvcResult mvcResult = this.mockMvc.perform(get("/categories")
								  .accept(ApiConfigConstants.API_V_2_0))
								  .andExpect(status().isOk())
								  .andExpect(content().contentTypeCompatibleWith(ApiConfigConstants.API_V_2_0))
								  .andExpect(content().encoding(StandardCharsets.UTF_8.name()))
								  .andExpect(content().contentType("application/vnd.naakcii.api-v2.0+json;charset=UTF-8"))
								  .andDo(print())
								  .andReturn();
		stopWatch.stop();
		logger.info("Execution of request '{}({})' has finished.", "GET", "/categories");
		logger.info("Execution time is: {} milliseconds.", stopWatch.getTotalTimeMillis());
		String resultJson = mvcResult.getResponse().getContentAsString();
		assertEquals("Expected JSON should be: ["
				   + "{\"name\":\"Молочные продукты, яйца\",\"priority\":2,\"icon\":\"Молочные продукты, яйца.png\",\"id\":2},"
				   + "{\"name\":\"Хлебобулочные изделия\",\"priority\":4,\"icon\":\"Хлебобулочные изделия.png\",\"id\":3},"
				   + "{\"name\":\"Мясо и колбасные изделия\",\"priority\":7,\"icon\":\"Мясо и колбасные изделия.png\",\"id\":1}"
				   + "].", expectedJson, resultJson);
		removeListOfCategories();
	}
	
	@After
	public void tearDown() {
		objectMapper = null;
		stopWatch = null;
		activeCategories = null;
		inactiveCategories = null;
	}
}
