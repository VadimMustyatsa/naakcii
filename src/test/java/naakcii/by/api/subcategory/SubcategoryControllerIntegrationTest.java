package naakcii.by.api.subcategory;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import naakcii.by.api.APIApplication;
import naakcii.by.api.category.Category;
import naakcii.by.api.config.ApiConfigConstants;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = APIApplication.class)
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SubcategoryControllerIntegrationTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private TestEntityManager testEntityManager;
	
	private ObjectMapper objectMapper;
	private Subcategory firstSubcategory;
	private Subcategory secondSubcategory;
	private Subcategory thirdSubcategory;
	private Subcategory fourthSubcategory;
	private Subcategory fifthSubcategory;
	private Long categoryId;
	
	@Before
	public void setUp() {
		objectMapper = new ObjectMapper();
	}
	
	private void createListOfSubcategories() {
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
		testEntityManager.persist(firstCategory);
		testEntityManager.persist(secondCategory);
		testEntityManager.persist(thirdCategory);
		testEntityManager.detach(firstCategory);
		testEntityManager.detach(secondCategory);
		testEntityManager.detach(thirdCategory);
		categoryId = firstCategory.getId();
		List<Subcategory> subcategories = new ArrayList<>();
		subcategories.add(secondSubcategory);
		subcategories.add(firstSubcategory);
		subcategories.add(fifthSubcategory);
	}
	
	@Test
	public void test_get_all_subcategories_by_category_id() throws Exception {
		createListOfSubcategories();
		List<SubcategoryDTO> expectedSubcategoryDTOs = new ArrayList<>();
		expectedSubcategoryDTOs.add(new SubcategoryDTO(secondSubcategory));
		expectedSubcategoryDTOs.add(new SubcategoryDTO(firstSubcategory));
		expectedSubcategoryDTOs.add(new SubcategoryDTO(fifthSubcategory));
		String expectedJson = objectMapper.writeValueAsString(expectedSubcategoryDTOs);
		MvcResult mvcResult = this.mockMvc.perform(get("/subcategories/{categoryId}", categoryId.toString())
								  .accept(ApiConfigConstants.API_V_2_0))
								  .andExpect(status().isOk())
								  .andExpect(content().encoding(StandardCharsets.UTF_8.name()))
								  .andExpect(content().contentType("application/vnd.naakcii.api-v2.0+json;charset=UTF-8"))
								  .andDo(print())
								  .andReturn();
		String resultJson = mvcResult.getResponse().getContentAsString();
		assertEquals("Expected json should be: ["
				   + "{\"id\":1,\"name\":\"2nd subcategory\",\"categoryId\":1,\"priority\":1},"
				   + "{\"id\":3,\"name\":\"1st subcategory\",\"categoryId\":1,\"priority\":7},"
				   + "{\"id\":2,\"name\":\"5th subcategory\",\"categoryId\":1,\"priority\":9}"
				   + "].", expectedJson, resultJson);
	}
	
	@After
	public void tearDown() {
		objectMapper = null;
		firstSubcategory = null;
		secondSubcategory = null;
		thirdSubcategory = null;
		fourthSubcategory = null;
		fifthSubcategory = null;
		categoryId = null;
	}
}
