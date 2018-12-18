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
	private List<Category> categories;
	
	@Before
	public void setUp() {
		objectMapper = new ObjectMapper();
		stopWatch = new StopWatch();
	}
		
	private void createListOfCategories() {
		logger.info("Preparing of test data.");
		Category firstCategory = new Category("First category", true);
		Category secondCategory = new Category("Second category", true);
		Category thirdCategory = new Category("Third category", true);
		firstCategory.setIcon("First category icon");
		firstCategory.setPriority(7);
		secondCategory.setIcon("Second category icon");
		secondCategory.setPriority(2);
		thirdCategory.setIcon("Third category icon");
		thirdCategory.setPriority(4);
		Subcategory firstSubcategory = new Subcategory("1st subcategory", firstCategory, true);
		firstSubcategory.setPriority(7);
		Subcategory secondSubcategory = new Subcategory("2nd subcategory", firstCategory, true);
		secondSubcategory.setPriority(1);
		Subcategory thirdSubcategory = new Subcategory("3rd subcategory", secondCategory, true);
		thirdSubcategory.setPriority(5);
		Subcategory fourthSubcategory = new Subcategory("4th subcategory", thirdCategory, true);
		fourthSubcategory.setPriority(3);
		categories = new ArrayList<>();
				
		try {
			categories.add(testEntityManager.persist(secondCategory));
			categories.add(testEntityManager.persist(thirdCategory));
			categories.add(testEntityManager.persist(firstCategory));
			testEntityManager.clear();
			logger.info("Test data was created successfully: instances of '{}' and '{}' were added in the database.",
					Category.class, Subcategory.class);
		} catch (Exception exception) {
			logger.error("Exception has occured during the creation of test data ('{}' and '{}' instances): {}.", exception);
		} 
	}
	
	private void removeListOfCategories() {	
		logger.info("Removing of test data.");
		
		try {
			categories.stream()
					  .map((Category category) -> testEntityManager.merge(category))
					  .forEach((Category category) ->	testEntityManager.remove(category));		  
			testEntityManager.flush();
			logger.info("Test data was cleaned successfully: instances of '{}' and '{}' were removed from the database.",
					Category.class, Subcategory.class);
		} catch (Exception exception) {
			logger.error("Exception has occured during the cleaning of test data ('{}' and '{}' instances): {}.", 
					Category.class, Subcategory.class, exception);
		}
	}
	
	@Test
	public void test_get_all_categories() throws Exception {
		createListOfCategories();
		List<CategoryDTO> expectedCategoryDTOs = categories
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
		logger.info("Execution time is: {} ms.", stopWatch.getTotalTimeMillis());
		String resultJson = mvcResult.getResponse().getContentAsString();
		assertEquals("Expected json should be: ["
				   + "{\"name\":\"Second category\",\"priority\":2,\"icon\":\"Second category icon\",\"id\":2},"
				   + "{\"name\":\"Third category\",\"priority\":4,\"icon\":\"Third category icon\",\"id\":3},"
				   + "{\"name\":\"First category\",\"priority\":7,\"icon\":\"First category icon\",\"id\":1}"
				   + "].", expectedJson, resultJson);
		removeListOfCategories();
	}
	
	@After
	public void tearDown() {
		objectMapper = null;
		stopWatch = null;
		categories = null;
	}
}
