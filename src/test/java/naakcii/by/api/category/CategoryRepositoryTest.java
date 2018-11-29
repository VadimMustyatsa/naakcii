package naakcii.by.api.category;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import naakcii.by.api.subcategory.Subcategory;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class CategoryRepositoryTest {
	
	@Autowired
	private TestEntityManager testEntityManager;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	private Category category;
	
	@Before
	public void setUp() {
		category = new Category("Category name", true);
		category.setIcon("Category icon");
		category.setPriority(5);
		Subcategory firstSubcategory = new Subcategory("1st subcategory", category, true);
		firstSubcategory.setPriority(8);
		Subcategory secondSubcategory = new Subcategory("2nd subcategory", category, true);
		secondSubcategory.setPriority(2);
		testEntityManager.persistAndFlush(category);
		testEntityManager.detach(category);
	}
	
	@Test
	public void test_soft_delete() {
		int numberOfUpdatedRows = categoryRepository.softDelete(category.getId());
		assertTrue("Number of updated rows in the database should be equal to 1, as 1 entity has been modified.", numberOfUpdatedRows == 1);
	}
	
	@Test
	public void test_soft_delete_with_wrong_id() {
		int numberOfUpdatedRows = categoryRepository.softDelete(category.getId() + 10);
		assertTrue("Number of updated rows in the database should be equal to 0, as nothing has been modified.", numberOfUpdatedRows == 0);
	}
	
	@After
	public void tearDown() {
		category = null;
	}
}
