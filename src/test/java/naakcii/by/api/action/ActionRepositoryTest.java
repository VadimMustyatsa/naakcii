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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import naakcii.by.api.actiontype.ActionType;
import naakcii.by.api.category.Category;
import naakcii.by.api.chain.Chain;
import naakcii.by.api.product.Product;
import naakcii.by.api.product.Unit;
import naakcii.by.api.subcategory.Subcategory;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-unit-test.properties")
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
	private Action sixthAction;
	private Action seventhAction;
	private Action eighthAction;
	private Action ninthAction;
	private Action tenthAction;
	private Action eleventhAction;
	private Action twelvethAction;
	private Action thirteenthAction;
	private Action fourteenthAction;
	private Action fifteenthAction;
	private Action sixteenthAction;
	private Long firstSubcategoryId;
	private Long secondSubcategoryId;
	private Long thirdSubcategoryId;
	private Long fourthSubcategoryId;
	private Long firstChainId;
	private Long secondChainId;
	private Long thirdChainId;
	private Long fourthChainId;
	
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
		Subcategory fourthSubcategory = new Subcategory("Fourth subcategory", category, true);
		Chain firstChain = new Chain("First chain", "First chain link", true);
		Chain secondChain = new Chain("Second chain", "Second chain link", true);
		Chain thirdChain = new Chain("Third chain", "Third chain link", true);
		Chain fourthChain = new Chain("Fourth chain", "Fourth chain link", true);
		ActionType firstActionType = new ActionType("First action type");
		ActionType secondActionType = new ActionType("Second action type");
		Product firstProduct = new Product("10000000000000", "First product", Unit.PC, true, firstSubcategory);
		Product secondProduct = new Product("20000000000000", "Second product", Unit.KG, true, firstSubcategory);
		Product thirdProduct = new Product("30000000000000", "Third product", Unit.PC, true, firstSubcategory);
		Product fourthProduct = new Product("40000000000000", "Fourth product", Unit.KG, true, secondSubcategory);
		Product fifthProduct = new Product("50000000000000", "Fifth product", Unit.PC, true, secondSubcategory);
		Product sixthProduct = new Product("60000000000000", "Sixth product", Unit.KG, true, thirdSubcategory);
		Product seventhProduct = new Product("70000000000000", "Seventh product", Unit.PC, true, thirdSubcategory);
		Product eighthProduct = new Product("80000000000000", "Eighth product", Unit.KG, true, fourthSubcategory);
		Product ninthProduct = new Product("90000000000000", "Ninth product", Unit.PC, false, firstSubcategory);
		Product tenthProduct = new Product("10000000000000", "Tenth product", Unit.KG, false, secondSubcategory);
		testEntityManager.persist(category);
		testEntityManager.persist(firstChain);
		testEntityManager.persist(secondChain);
		testEntityManager.persist(thirdChain);
		testEntityManager.persist(fourthChain);
		testEntityManager.persist(firstActionType);
		testEntityManager.persist(secondActionType);
		Calendar firstStartDate = getCurrentDate();
		firstStartDate.add(Calendar.DAY_OF_MONTH, -7);
		Calendar firstEndDate = getCurrentDate();
		firstEndDate.add(Calendar.DAY_OF_MONTH, 7);
		firstAction = new Action(firstProduct, firstChain, new BigDecimal("5.50"), firstActionType, firstStartDate, firstEndDate);
		Calendar secondStartDate = getCurrentDate();
		secondStartDate.add(Calendar.DAY_OF_MONTH, -14);
		Calendar secondEndDate = getCurrentDate();
		secondEndDate.add(Calendar.DAY_OF_MONTH, 7);
		secondAction = new Action(firstProduct, secondChain, new BigDecimal("6.75"), firstActionType, secondStartDate, secondEndDate);
		Calendar thirdStartDate = getCurrentDate();
		Calendar thirdEndDate = getCurrentDate();
		thirdEndDate.add(Calendar.DAY_OF_MONTH, 14);
		thirdAction = new Action(firstProduct, thirdChain, new BigDecimal("2.25"), secondActionType, thirdStartDate, thirdEndDate);
		Calendar fourthStartDate = getCurrentDate();
		fourthStartDate.add(Calendar.MONTH, -1);
		Calendar fourthEndDate = getCurrentDate();
		fourthEndDate.add(Calendar.MONTH, 1);
		fourthAction = new Action(secondProduct, secondChain, new BigDecimal("12.50"), secondActionType, fourthStartDate, fourthEndDate);
		Calendar fifthStartDate = getCurrentDate();
		fifthStartDate.add(Calendar.DAY_OF_MONTH, -5);
		Calendar fifthEndDate = getCurrentDate();
		fifthEndDate.add(Calendar.DAY_OF_MONTH, 5);
		fifthAction = new Action(secondProduct, thirdChain, new BigDecimal("5.25"), secondActionType, fifthStartDate, fifthEndDate);
		Calendar sixthStartDate = getCurrentDate();
		sixthStartDate.add(Calendar.DAY_OF_MONTH, -10);
		Calendar sixthEndDate = getCurrentDate();
		sixthEndDate.add(Calendar.DAY_OF_MONTH, 10);
		sixthAction = new Action(secondProduct, fourthChain, new BigDecimal("15.50"), firstActionType, sixthStartDate, sixthEndDate);
		Calendar seventhStartDate = getCurrentDate();
		seventhStartDate.add(Calendar.DAY_OF_MONTH, -15);
		Calendar seventhEndDate = getCurrentDate();
		seventhEndDate.add(Calendar.DAY_OF_MONTH, 15);
		seventhAction = new Action(thirdProduct, firstChain, new BigDecimal("2.50"), firstActionType, seventhStartDate, seventhEndDate);
		Calendar eighthStartDate = getCurrentDate();
		eighthStartDate.add(Calendar.DAY_OF_MONTH, -20);
		Calendar eighthEndDate = getCurrentDate();
		eighthEndDate.add(Calendar.DAY_OF_MONTH, 20);
		eighthAction = new Action(fourthProduct, secondChain, new BigDecimal("1.75"), firstActionType, eighthStartDate, eighthEndDate);
		Calendar ninthStartDate = getCurrentDate();
		ninthStartDate.add(Calendar.DAY_OF_MONTH, -7);
		Calendar ninthEndDate = getCurrentDate();
		ninthEndDate.add(Calendar.DAY_OF_MONTH, 14);
		ninthAction = new Action(fifthProduct, thirdChain, new BigDecimal("10.75"), secondActionType, ninthStartDate, ninthEndDate);
		Calendar tenthStartDate = getCurrentDate();
		tenthStartDate.add(Calendar.DAY_OF_MONTH, -14);
		Calendar tenthEndDate = getCurrentDate();
		tenthEndDate.add(Calendar.DAY_OF_MONTH, 7);
		tenthAction = new Action(sixthProduct, fourthChain, new BigDecimal("5.50"), secondActionType, tenthStartDate, tenthEndDate);
		Calendar eleventhStartDate = getCurrentDate();
		eleventhStartDate.add(Calendar.DAY_OF_MONTH, -5);
		Calendar eleventhEndDate = getCurrentDate();
		eleventhEndDate.add(Calendar.DAY_OF_MONTH, 5);
		eleventhAction = new Action(seventhProduct, secondChain, new BigDecimal("7.75"), firstActionType, eleventhStartDate, eleventhEndDate);
		Calendar twelvethStartDate = getCurrentDate();
		twelvethStartDate.add(Calendar.DAY_OF_MONTH, -21);
		Calendar twelvethEndDate = getCurrentDate();
		twelvethEndDate.add(Calendar.DAY_OF_MONTH, 21);
		twelvethAction = new Action(seventhProduct, thirdChain, new BigDecimal("1.00"), secondActionType, twelvethStartDate, twelvethEndDate);
		Calendar thirteenthStartDate = getCurrentDate();
		thirteenthStartDate.add(Calendar.DAY_OF_MONTH, -7);
		Calendar thirteenthEndDate = getCurrentDate();
		thirteenthEndDate.add(Calendar.DAY_OF_MONTH, 21);
		thirteenthAction = new Action(eighthProduct, firstChain, new BigDecimal("4.00"), firstActionType, thirteenthStartDate, thirteenthEndDate);
		Calendar fourteenthStartDate = getCurrentDate();
		fourteenthStartDate.add(Calendar.DAY_OF_MONTH, -28);
		Calendar fourteenthEndDate = getCurrentDate();
		fourteenthEndDate.add(Calendar.DAY_OF_MONTH, 28);
		fourteenthAction = new Action(eighthProduct, fourthChain, new BigDecimal("15.00"), secondActionType, fourteenthStartDate, fourteenthEndDate);
		Calendar fifteenthStartDate = getCurrentDate();
		fifteenthStartDate.add(Calendar.DAY_OF_MONTH, -15);
		Calendar fifteenthEndDate = getCurrentDate();
		fifteenthEndDate.add(Calendar.DAY_OF_MONTH, 15);
		fifteenthAction = new Action(ninthProduct, secondChain, new BigDecimal("0.85"), firstActionType, fifteenthStartDate, fifteenthEndDate);
		Calendar sixteenthStartDate = getCurrentDate();
		sixteenthStartDate.add(Calendar.DAY_OF_MONTH, -5);
		Calendar sixteenthEndDate = getCurrentDate();
		sixteenthEndDate.add(Calendar.DAY_OF_MONTH, 5);
		sixteenthAction = new Action(tenthProduct, thirdChain, new BigDecimal("1.15"), secondActionType, sixteenthStartDate, sixteenthEndDate);
		testEntityManager.flush();
		firstSubcategoryId = firstSubcategory.getId();
		secondSubcategoryId = secondSubcategory.getId();
		thirdSubcategoryId = thirdSubcategory.getId();
		fourthSubcategoryId = fourthSubcategory.getId();
		firstChainId = firstChain.getId();
		secondChainId = secondChain.getId();
		thirdChainId = thirdChain.getId();
		fourthChainId = fourthChain.getId();
		testEntityManager.detach(firstActionType);
		testEntityManager.detach(secondActionType);
		testEntityManager.detach(firstChain);
		testEntityManager.detach(secondChain);
		testEntityManager.detach(thirdChain);
		testEntityManager.detach(fourthChain);
		testEntityManager.detach(category);
	}
		
	@Test
	public void test_find_all_by_subcategory_id() {
		List<Action> actions = actionRepository.findByProductIsActiveTrueAndProductSubcategoryIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
				thirdSubcategoryId, Calendar.getInstance(), Calendar.getInstance());
		assertEquals("Number actions, that have been found in the database, should be 3: [10thAction, 11thAction, 12thAction].", 3, actions.size());
		assertTrue("Result list of actions should contain 10th action.", actions.contains(tenthAction));
		assertTrue("Result list of actions should contain 11th action.", actions.contains(eleventhAction));
		assertTrue("Result list of actions should contain 12th action.", actions.contains(twelvethAction));
	}
	
	@Test
	public void test_find_all_by_subcategories_ids() {
		Set<Long> subcategoryIds = new HashSet<>();
		subcategoryIds.add(firstSubcategoryId);
		subcategoryIds.add(secondSubcategoryId);
		List<Action> actions = actionRepository.findByProductIsActiveTrueAndProductSubcategoryIdInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
				subcategoryIds, Calendar.getInstance(), Calendar.getInstance());
		assertEquals("Number actions, that have been found in the database, should be 9: "
				+ "[1stAction, 2ndAction, 3rdAction, 4thAction, 5thAction, 6thAction, 7thAction, 8thAction, 9thAction].", 9, actions.size());
		assertTrue("Result list of actions should contain 1st action.", actions.contains(firstAction));
		assertTrue("Result list of actions should contain 2nd action.", actions.contains(secondAction));
		assertTrue("Result list of actions should contain 3rd action.", actions.contains(thirdAction));
		assertTrue("Result list of actions should contain 4th action.", actions.contains(fourthAction));
		assertTrue("Result list of actions should contain 5th action.", actions.contains(fifthAction));
		assertTrue("Result list of actions should contain 6th action.", actions.contains(sixthAction));
		assertTrue("Result list of actions should contain 7th action.", actions.contains(seventhAction));
		assertTrue("Result list of actions should contain 8th action.", actions.contains(eighthAction));
		assertTrue("Result list of actions should contain 9th action.", actions.contains(ninthAction));
	}
	
	@Test
	public void test_find_all_by_subcategories_ids_and_chain_ids() {
		Set<Long> subcategoryIds = new HashSet<>();
		subcategoryIds.add(firstSubcategoryId);
		subcategoryIds.add(secondSubcategoryId);
		subcategoryIds.add(fourthSubcategoryId);
		Set<Long> chainIds = new HashSet<>();
		chainIds.add(secondChainId);
		chainIds.add(thirdChainId);
		List<Action> actions = actionRepository.findByProductIsActiveTrueAndProductSubcategoryIdInAndChainIdInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
				subcategoryIds, chainIds, Calendar.getInstance(), Calendar.getInstance());
		assertEquals("Number actions, that have been found in the database, should be 6: "
				+ "[2ndAction, 3rdAction, 4thAction, 5thAction, 8thAction, 9thAction].", 6, actions.size());
		assertTrue("Result list of actions should contain 2nd action.", actions.contains(secondAction));
		assertTrue("Result list of actions should contain 3rd action.", actions.contains(thirdAction));
		assertTrue("Result list of actions should contain 4th action.", actions.contains(fourthAction));
		assertTrue("Result list of actions should contain 5th action.", actions.contains(fifthAction));
		assertTrue("Result list of actions should contain 8th action.", actions.contains(eighthAction));
		assertTrue("Result list of actions should contain 9th action.", actions.contains(ninthAction));
	}
	
	@Test
	public void test_find_all_by_subcategories_ids_and_chain_ids_when_page_number_is_1_and_page_size_is_4() {
		Pageable pageRequest = PageRequest.of(0, 4, Sort.DEFAULT_DIRECTION, "discountPrice");
		Set<Long> subcategoryIds = new HashSet<>();
		subcategoryIds.add(firstSubcategoryId);
		subcategoryIds.add(secondSubcategoryId);
		subcategoryIds.add(fourthSubcategoryId);
		Set<Long> chainIds = new HashSet<>();
		chainIds.add(secondChainId);
		chainIds.add(thirdChainId);
		List<Action> actions = actionRepository.findByProductIsActiveTrueAndProductSubcategoryIdInAndChainIdInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
				subcategoryIds, chainIds, Calendar.getInstance(), Calendar.getInstance(), pageRequest);
		assertEquals("Number actions, that have been found in the database and placed on the first page, should be 4: ["
				+ "8thAction (discountPrice = 1.75), "
				+ "3rdAction (discountPrice = 2.25), "
				+ "5thAction (discountPrice = 5.25), "
				+ "2ndAction (discountPrice = 6.75)"
				+ "].", 4, actions.size());
		assertTrue("Result list of actions should contain 8th action.", actions.contains(eighthAction));
		assertTrue("Result list of actions should contain 3rd action.", actions.contains(thirdAction));
		assertTrue("Result list of actions should contain 5th action.", actions.contains(fifthAction));
		assertTrue("Result list of actions should contain 2nd action.", actions.contains(secondAction));
	}
	
	@Test
	public void test_find_all_by_subcategories_ids_and_chain_ids_when_page_number_is_2_and_page_size_is_4() {
		Pageable pageRequest = PageRequest.of(1, 4, Sort.DEFAULT_DIRECTION, "discountPrice");
		Set<Long> subcategoryIds = new HashSet<>();
		subcategoryIds.add(firstSubcategoryId);
		subcategoryIds.add(secondSubcategoryId);
		subcategoryIds.add(fourthSubcategoryId);
		Set<Long> chainIds = new HashSet<>();
		chainIds.add(secondChainId);
		chainIds.add(thirdChainId);
		List<Action> actions = actionRepository.findByProductIsActiveTrueAndProductSubcategoryIdInAndChainIdInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
				subcategoryIds, chainIds, Calendar.getInstance(), Calendar.getInstance(), pageRequest);
		assertEquals("Number actions, that have been found in the database and placed on the second page, should be 2: ["
				+ "9thAction (discountPrice = 10.75), "
				+ "4thAction (discountPrice = 12.50), "
				+ "].", 2, actions.size());
		assertTrue("Result list of actions should contain 9th action.", actions.contains(ninthAction));
		assertTrue("Result list of actions should contain 4th action.", actions.contains(fourthAction));
	}
	
	@Test
	public void test_find_all_by_subcategories_ids_and_chain_ids_when_page_number_is_3_and_page_size_is_4() {
		Pageable pageRequest = PageRequest.of(2, 4, Sort.DEFAULT_DIRECTION, "discountPrice");
		Set<Long> subcategoryIds = new HashSet<>();
		subcategoryIds.add(firstSubcategoryId);
		subcategoryIds.add(secondSubcategoryId);
		subcategoryIds.add(fourthSubcategoryId);
		Set<Long> chainIds = new HashSet<>();
		chainIds.add(secondChainId);
		chainIds.add(thirdChainId);
		List<Action> actions = actionRepository.findByProductIsActiveTrueAndProductSubcategoryIdInAndChainIdInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
				subcategoryIds, chainIds, Calendar.getInstance(), Calendar.getInstance(), pageRequest);
		assertEquals("Number actions, that have been found in the database and placed on the second page, should be 0, as all results have been placed on two previous pages.", 0, actions.size());
	}
	
	@After
	public void tearDown() {
		firstAction = null;
		secondAction = null;
		thirdAction = null;
		fourthAction = null;
		fifthAction = null;
		sixthAction = null;
		seventhAction = null;
		eighthAction = null;
		ninthAction = null;
		tenthAction = null;
		eleventhAction = null;
		twelvethAction = null;
		thirteenthAction = null;
		fourteenthAction = null;
		fifteenthAction = null;
		sixteenthAction = null;
		firstSubcategoryId = null;
		secondSubcategoryId = null;
		thirdSubcategoryId = null;
		fourthSubcategoryId = null;
		firstChainId = null;
		secondChainId = null;
		thirdChainId = null;
		fourthChainId = null;
	}
}
