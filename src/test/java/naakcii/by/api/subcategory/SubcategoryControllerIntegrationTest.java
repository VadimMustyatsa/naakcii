package naakcii.by.api.subcategory;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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
import naakcii.by.api.category.Category;
import naakcii.by.api.config.ApiConfigConstants;

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
		Category firstCategory = new Category("First category", true);
		Category secondCategory = new Category("Second category", true);
		Category thirdCategory = new Category("Third category", true);
		firstCategory.setIcon("First category icon");
		firstCategory.setPriority(1);
		secondCategory.setIcon("Second category icon");
		secondCategory.setPriority(2);
		thirdCategory.setIcon("Third category icon");
		thirdCategory.setPriority(3);
		firstSubcategory = new Subcategory("1st subcategory", firstCategory, true);
		firstSubcategory.setPriority(7);
		secondSubcategory = new Subcategory("2nd subcategory", firstCategory, true);
		secondSubcategory.setPriority(1);
		thirdSubcategory = new Subcategory("3rd subcategory", secondCategory, true);
		thirdSubcategory.setPriority(5);
		fourthSubcategory = new Subcategory("4th subcategory", thirdCategory, true);
		fourthSubcategory.setPriority(3);
		fifthSubcategory = new Subcategory("5th subcategory", firstCategory, true);
		fifthSubcategory.setPriority(9);
		sixthSubcategory = new Subcategory("6th subcategory", secondCategory, false);
		sixthSubcategory.setPriority(2);
		seventhSubcategory = new Subcategory("7th subcategory", thirdCategory, false);
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
			categories.stream()
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
				   + "{\"id\":1,\"name\":\"2nd subcategory\",\"categoryId\":1,\"priority\":1},"
				   + "{\"id\":3,\"name\":\"1st subcategory\",\"categoryId\":1,\"priority\":7},"
				   + "{\"id\":2,\"name\":\"5th subcategory\",\"categoryId\":1,\"priority\":9}"
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
