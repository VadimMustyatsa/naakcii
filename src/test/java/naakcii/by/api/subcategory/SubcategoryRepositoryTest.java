package naakcii.by.api.subcategory;

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

import naakcii.by.api.category.Category;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-unit-test.properties")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class SubcategoryRepositoryTest {
	
	@Autowired
	private TestEntityManager testEntityManager;
	
	@Autowired
	private SubcategoryRepository subcategoryRepository;
	
	private Subcategory subcategory;
	
	@Before
	public void setUp() {
		Category category = new Category("Category name", true);
		category.setPriority(10);
		category.setIcon("Category icon");
		subcategory = new Subcategory("Subcategory name", category, true);
		subcategory.setPriority(2);
		testEntityManager.persistAndFlush(category);
		testEntityManager.detach(category);
	}
	
	@Test
	public void test_soft_delete() {
		int numberOfUpdatedRows = subcategoryRepository.softDelete(subcategory.getId());
		assertTrue("Number of updated rows in the database should be equal to 1, as 1 entity has been modified.", numberOfUpdatedRows == 1);
	}
	
	@Test
	public void test_soft_delete_with_wrong_id() {
		int numberOfUpdatedRows = subcategoryRepository.softDelete(subcategory.getId() + 10);
		assertTrue("Number of updated rows in the database should be equal to 0, as nothing has been modified.", numberOfUpdatedRows == 0);
	}

	@After
	public void tearDown() {
		subcategory = null;
	}
}
