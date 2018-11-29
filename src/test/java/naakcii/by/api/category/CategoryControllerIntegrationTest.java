package naakcii.by.api.category;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import naakcii.by.api.APIApplication;
import naakcii.by.api.subcategory.Subcategory;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = APIApplication.class)
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CategoryControllerIntegrationTest {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Before
	public void setUp() {
		Category firstCategory = new Category("First category", true);
		Category secondCategory = new Category("Second category", true);
		Category thirdCategory = new Category("Third category", true);
		firstCategory.setIcon("First category icon");
		firstCategory.setPriority(3);
		secondCategory.setIcon("Second category icon");
		secondCategory.setPriority(2);
		thirdCategory.setIcon("Third category icon");
		thirdCategory.setPriority(1);
		Subcategory firstSubcategory = new Subcategory("1st subcategory", firstCategory, true);
		firstSubcategory.setPriority(7);
		Subcategory secondSubcategory = new Subcategory("2nd subcategory", firstCategory, true);
		secondSubcategory.setPriority(1);
		Subcategory thirdSubcategory = new Subcategory("3rd subcategory", secondCategory, true);
		thirdSubcategory.setPriority(5);
		Subcategory fourthSubcategory = new Subcategory("4th subcategory", thirdCategory, true);
		fourthSubcategory.setPriority(3);
		categoryRepository.save(firstCategory);
		categoryRepository.save(secondCategory);
		categoryRepository.save(thirdCategory);
	}
	
	@Test
	public void test_get_all_categories() throws Exception {
		
	}

}
