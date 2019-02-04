package naakcii.by.api.subcategory;

import naakcii.by.api.category.Category;

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

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-unit-test.properties")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class SubcategoryRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    private Category category;
    private Subcategory firstSubcategory;
    private Subcategory secondSubcategory;
    private Subcategory thirdSubcategory;

    @Before
    public void setUp() {
        category = new Category("Мясо и колбасные изделия", true);
        category.setPriority(10);
        category.setIcon("Мясо и колбасные изделия.png");
        firstSubcategory = new Subcategory("Колбасные изделия", true, category);
        firstSubcategory.setPriority(1);
        secondSubcategory = new Subcategory("Копчености", false, category);
        secondSubcategory.setPriority(2);
        thirdSubcategory = new Subcategory("Свинина", true, category);
        thirdSubcategory.setPriority(3);
        testEntityManager.persistAndFlush(category);
        testEntityManager.detach(category);
    }

    @Test
    public void test_soft_delete() {
        int numberOfUpdatedRows = subcategoryRepository.softDelete(firstSubcategory.getId());
        assertTrue("Number of updated rows in the database should be equal to 1, as 1 entity has been modified.", numberOfUpdatedRows == 1);
    }

    @Test
    public void test_soft_delete_with_wrong_id() {
        int numberOfUpdatedRows = subcategoryRepository.softDelete(firstSubcategory.getId() + 10);
        assertTrue("Number of updated rows in the database should be equal to 0, as nothing has been modified.", numberOfUpdatedRows == 0);
    }

    @Test
    public void test_find_by_is_active_true_and_category_id_order_by_priority_asc() {
    	List<Subcategory> expectedSubcategories = new ArrayList<>();
    	expectedSubcategories.add(firstSubcategory);
    	expectedSubcategories.add(thirdSubcategory);
        List<Subcategory> resultSubcategories = subcategoryRepository.findByIsActiveTrueAndCategoryIdOrderByPriorityAsc(category.getId());
        assertEquals("Result list of subcategories should contain 2 elements.", 2,  resultSubcategories.size());
        assertEquals("Result list of subcategories should be: ["
        		+ "{name:'Колбасные изделия', isActive:true, priority:1},"
        		+ "{name:'Свинина', isActive:true, priority:3}"
        		+ "]", expectedSubcategories, resultSubcategories);
    }

    @Test
    public void test_find_by_is_active_true_and_category_id_order_by_priority_desc() {
    	List<Subcategory> expectedSubcategories = new ArrayList<>();
    	expectedSubcategories.add(thirdSubcategory);
    	expectedSubcategories.add(firstSubcategory);
        List<Subcategory> resultSubcategories = subcategoryRepository.findByIsActiveTrueAndCategoryIdOrderByPriorityDesc(category.getId());
        assertEquals("Result list of subcategories should contain 2 elements.", 2,  resultSubcategories.size());
        assertEquals("Result list of subcategories should be: ["
        		+ "{name:'Свинина', isActive:true},"
        		+ "{name:'Колбасные изделия', isActive:true}"
        		+ "]", expectedSubcategories, resultSubcategories);
    }

    @Test
    public void test_find_by_name_and_category_name() {
    	Subcategory expectedSubcategory = secondSubcategory;
        Subcategory resultSubcategory = subcategoryRepository.findByNameAndCategoryName(secondSubcategory.getName(), category.getName()).get();
        assertEquals("Result subcategory should be: {name:'Копчености', isActive:false, priority:2}.", expectedSubcategory, resultSubcategory);
    }
    
    @After
    public void tearDown() {
    	category = null;
    	firstSubcategory = null;
    	secondSubcategory = null;
    	thirdSubcategory = null;
    }
}
