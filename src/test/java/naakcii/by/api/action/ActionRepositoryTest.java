package naakcii.by.api.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import naakcii.by.api.actiontype.ActionType;
import naakcii.by.api.category.Category;
import naakcii.by.api.chain.Chain;
import naakcii.by.api.product.Product;
import naakcii.by.api.subcategory.Subcategory;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ActionRepositoryTest {
	
	private static Calendar currentDate = Calendar.getInstance();
	
	@Autowired
	private TestEntityManager testEntityManager;
	
	@Autowired
	private ActionRepository actionRepository;
	
	private Action firstAction;
	private Action secondAction;
	private Action thirdAction;
	private Action fourthAction;
	private Action fifthAction;
	private Long firstSubcategoryId;
	private Long secondSubcategoryId;
	private Long thirdSubcategoryId;
	private Long firstChainId;
	private Long secondChainId;
	private Long thirdChainId;
	
	private Calendar getCurrentDate() {
		return new GregorianCalendar(currentDate.get(Calendar.YEAR), 
									 currentDate.get(Calendar.MONTH), 
									 currentDate.get(Calendar.DAY_OF_MONTH)
		);
	}
	
	@Before
	public void setUp() {
		Category category = new Category("Category name", true);
		category.setIcon("Category icon");
		Subcategory firstSubcategory = new Subcategory("First subcategory", category, true);
		Subcategory secondSubcategory = new Subcategory("Second subcategory", category, true);
		Subcategory thirdSubcategory = new Subcategory("Third subcategory", category, true);
		Chain firstChain = new Chain("First chain", "First chain link", true);
		Chain secondChain = new Chain("Second chain", "Second chain link", true);
		Chain thirdChain = new Chain("Third chain", "Third chain link", true);
		ActionType firstActionType = new ActionType("First action type");
		ActionType secondActionType = new ActionType("Second action type");
		Product firstProduct = new Product("10000000000000", "First product", true, firstSubcategory);
		Product secondProduct = new Product("20000000000000", "Second product", true, firstSubcategory);
		Product thirdProduct = new Product("30000000000000", "Third product", true, secondSubcategory);
		Product fourthProduct = new Product("40000000000000", "Fourth product", true, thirdSubcategory);
		testEntityManager.persist(category);
		testEntityManager.persist(firstChain);
		testEntityManager.persist(secondChain);
		testEntityManager.persist(thirdChain);
		testEntityManager.persist(firstActionType);
		testEntityManager.persist(secondActionType);
		Calendar firstStartDate = getCurrentDate();
		firstStartDate.add(Calendar.DAY_OF_MONTH, -7);
		Calendar firstEndDate = getCurrentDate();
		firstEndDate.add(Calendar.DAY_OF_MONTH, 7);
		firstAction = new Action(firstProduct, firstChain, new BigDecimal("5.50"), firstActionType, firstStartDate, firstEndDate);
		Calendar secondStartDate = getCurrentDate();
		secondStartDate.add(Calendar.DAY_OF_MONTH, -10);
		Calendar secondEndDate = getCurrentDate();
		secondEndDate.add(Calendar.DAY_OF_MONTH, 10);
		secondAction = new Action(secondProduct, firstChain, new BigDecimal("6.75"), firstActionType, secondStartDate, secondEndDate);
		Calendar thirdStartDate = getCurrentDate();
		Calendar thirdEndDate = getCurrentDate();
		thirdEndDate.add(Calendar.DAY_OF_MONTH, 14);
		thirdAction = new Action(thirdProduct, secondChain, new BigDecimal("2.25"), secondActionType, thirdStartDate, thirdEndDate);
		Calendar fourthStartDate = getCurrentDate();
		fourthStartDate.add(Calendar.MONTH, -1);
		Calendar fourthEndDate = getCurrentDate();
		fourthEndDate.add(Calendar.MONTH, 1);
		fourthAction = new Action(fourthProduct, secondChain, new BigDecimal("12.50"), secondActionType, fourthStartDate, fourthEndDate);
		Calendar fifthStartDate = getCurrentDate();
		fifthStartDate.add(Calendar.DAY_OF_MONTH, -5);
		Calendar fifthEndDate = getCurrentDate();
		fifthEndDate.add(Calendar.DAY_OF_MONTH, 5);
		fifthAction = new Action(firstProduct, thirdChain, new BigDecimal("5.25"), firstActionType, fifthStartDate, fifthEndDate);
		testEntityManager.flush();
		firstSubcategoryId = firstSubcategory.getId();
		secondSubcategoryId = secondSubcategory.getId();
		thirdSubcategoryId = thirdSubcategory.getId();
		firstChainId = firstChain.getId();
		secondChainId = secondChain.getId();
		thirdChainId = thirdChain.getId();
		testEntityManager.detach(firstActionType);
		testEntityManager.detach(secondActionType);
		testEntityManager.detach(firstChain);
		testEntityManager.detach(secondChain);
		testEntityManager.detach(thirdChain);
		testEntityManager.detach(category);
	}
	
	@Test
	public void test_find_all_by_subcategory_id() {
		List<Action> actions = actionRepository.findAllBySubcategoryId(thirdSubcategoryId, Calendar.getInstance());
		assertEquals("Number actions, that have been found in the database, should be 1, as only product from the fourth action pertains to the third subcategory.", actions.size(), 1);
		assertTrue("Result list of actions should contain fourth action.", actions.contains(fourthAction));
	}
	
	@Test
	public void test_find_all_by_subcategories_ids() {
		Set<Long> subcategoriesIds = new HashSet<>();
		subcategoriesIds.add(firstSubcategoryId);
		subcategoriesIds.add(secondSubcategoryId);
		List<Action> actions = actionRepository.findAllBySubcategoriesIds(subcategoriesIds, Calendar.getInstance());
		assertEquals("Number actions, that have been found in the database, should be 4, as "
				+ "products from the first, the second and the fifth actions pertain to the first subcategory "
				+ "and product from the third action pertains to the second subcategory.", actions.size(), 4);
		assertTrue("Result list of actions should contain first action.", actions.contains(firstAction));
		assertTrue("Result list of actions should contain second action.", actions.contains(secondAction));
		assertTrue("Result list of actions should contain third action.", actions.contains(thirdAction));
		assertTrue("Result list of actions should contain fifth action.", actions.contains(fifthAction));
	}
	
	@Test
	public void test_find_all_by_subcategories_ids_and_chain_ids() {
		Set<Long> subcategoriesIds = new HashSet<>();
		subcategoriesIds.add(firstSubcategoryId);
		subcategoriesIds.add(secondSubcategoryId);
		Set<Long> chainIds = new HashSet<>();
		chainIds.add(firstChainId);
		chainIds.add(thirdChainId);
		//List<Action> actions = actionRepository.findAllBySubcategoriesIdsAndChainIds(subcategoriesIds, chainIds, Calendar.getInstance());
		List<Action> actions = actionRepository.findByProductSubcategoryIdInAndChainIdInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(subcategoriesIds, chainIds, Calendar.getInstance(), Calendar.getInstance());
		assertEquals("Number actions, that have been found in the database, should be 3, as "
				+ "products from the first and the second actions pertain to the first subcategory and first action belongs to the first chain "
				+ "and product from the fifth action pertains to the first subcategory and fifth action belongs to the third chain.", actions.size(), 3);
		assertTrue("Result list of actions should contain first action.", actions.contains(firstAction));
		assertTrue("Result list of actions should contain second action.", actions.contains(secondAction));
		assertTrue("Result list of actions should contain fifth action.", actions.contains(fifthAction));
	}
	
	@After
	public void tearDown() {
		firstAction = null;
		secondAction = null;
		thirdAction = null;
		fourthAction = null;
		fifthAction = null;
		firstSubcategoryId = null;
		secondSubcategoryId = null;
		thirdSubcategoryId = null;
		firstChainId = null;
		secondChainId = null;
		thirdChainId = null;
	}
}
