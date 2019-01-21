package naakcii.by.api.actionproduct;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
import naakcii.by.api.action.Action;
import naakcii.by.api.actionproduct.ActionProductDTO;
import naakcii.by.api.actiontype.ActionType;
import naakcii.by.api.category.Category;
import naakcii.by.api.chain.Chain;
import naakcii.by.api.config.ApiConfigConstants;
import naakcii.by.api.country.Country;
import naakcii.by.api.country.CountryCode;
import naakcii.by.api.product.Product;
import naakcii.by.api.product.Unit;
import naakcii.by.api.subcategory.Subcategory;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = APIApplication.class)
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
@Transactional
@TestPropertySource(locations = "classpath:application-integration-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ActionProductControllerIntegrationTest {
	
	private static final Logger logger = LogManager.getLogger(ActionProductControllerIntegrationTest.class);

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private TestEntityManager testEntityManager;
	
	private ObjectMapper objectMapper;
	private StopWatch stopWatch;
	private List<ActionType> actionTypes;
	private List<Category> categories;
	private List<Chain> chains;
	private List<Country> countries;
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
		Calendar currentDate = Calendar.getInstance();
		return new GregorianCalendar(currentDate.get(Calendar.YEAR), 
									 currentDate.get(Calendar.MONTH), 
									 currentDate.get(Calendar.DAY_OF_MONTH)
		);
	}
	
	@Before
	public void setUp() {
		objectMapper = new ObjectMapper();
		stopWatch = new StopWatch();
	}
	
	private void createTestData() {
		logger.info("Preparing of test data.");
		//Creation of categories.
		Category firstCategory = new Category("First category name", true);
		firstCategory.setIcon("First category icon");
		Category secondCategory = new Category("Second category name", true);
		secondCategory.setIcon("Second category icon");
		//Creation of subcategories.
		Subcategory firstSubcategory = new Subcategory("First subcategory", firstCategory, true);
		Subcategory secondSubcategory = new Subcategory("Second subcategory", firstCategory, true);
		Subcategory thirdSubcategory = new Subcategory("Third subcategory", secondCategory, true);
		Subcategory fourthSubcategory = new Subcategory("Fourth subcategory", secondCategory, true);
		//Creation of chains.
		Chain firstChain = new Chain("First chain", "First chain link", true);
		Chain secondChain = new Chain("Second chain", "Second chain link", true);
		Chain thirdChain = new Chain("Third chain", "Third chain link", true);
		Chain fourthChain = new Chain("Fourth chain", "Fourth chain link", true);
		//Creation of action types.
		ActionType firstActionType = new ActionType("First action type");
		firstActionType.setTooltip("First action type tooltip text.");
		ActionType secondActionType = new ActionType("Second action type");
		secondActionType.setTooltip("Second action type tooltip text.");
		//Creation of countries.
		Country firstCountry = new Country(CountryCode.BY);
		Country secondCountry = new Country(CountryCode.LT);
		//Creation of products.
		Product firstProduct = new Product("10000000000000", "First product", Unit.KG, true, firstSubcategory);
		firstProduct.setManufacturer("First product manufacturer");
		firstProduct.setBrand("First product brand");
		firstProduct.setCountryOfOrigin(firstCountry);
		firstProduct.setPicture("Path to the picture of the first product");
		Product secondProduct = new Product("20000000000000", "Second product", Unit.KG, true, firstSubcategory);
		secondProduct.setManufacturer("Second product manufacturer");
		secondProduct.setBrand("First product brand");
		secondProduct.setCountryOfOrigin(secondCountry);
		secondProduct.setPicture("Path to the picture of the second product");
		Product thirdProduct = new Product("30000000000000", "Third product", Unit.KG, true, firstSubcategory);
		thirdProduct.setManufacturer("Third product manufacturer");
		thirdProduct.setBrand("First product brand");
		thirdProduct.setCountryOfOrigin(firstCountry);
		thirdProduct.setPicture("Path to the picture of the third product");
		Product fourthProduct = new Product("40000000000000", "Fourth product", Unit.PC, true, secondSubcategory);
		fourthProduct.setManufacturer("Fourth product manufacturer");
		fourthProduct.setBrand("First product brand");
		fourthProduct.setCountryOfOrigin(secondCountry);
		fourthProduct.setPicture("Path to the picture of the fourth product");
		Product fifthProduct = new Product("50000000000000", "Fifth product", Unit.KG, true, secondSubcategory);
		fifthProduct.setManufacturer("Fifth product manufacturer");
		fifthProduct.setBrand("First product brand");
		fifthProduct.setCountryOfOrigin(firstCountry);
		fifthProduct.setPicture("Path to the picture of the fifth product");
		Product sixthProduct = new Product("60000000000000", "Sixth product", Unit.PC, true, thirdSubcategory);
		sixthProduct.setManufacturer("Sixth product manufacturer");
		sixthProduct.setBrand("First product brand");
		sixthProduct.setCountryOfOrigin(secondCountry);
		sixthProduct.setPicture("Path to the picture of the sixth product");
		Product seventhProduct = new Product("70000000000000", "Seventh product", Unit.KG, true, thirdSubcategory);
		seventhProduct.setManufacturer("Seventh product manufacturer");
		seventhProduct.setBrand("First product brand");
		seventhProduct.setCountryOfOrigin(firstCountry);
		seventhProduct.setPicture("Path to the picture of the seventh product");
		Product eighthProduct = new Product("80000000000000", "Eighth product", Unit.PC, true, fourthSubcategory);
		eighthProduct.setManufacturer("Eighth product manufacturer");
		eighthProduct.setBrand("First product brand");
		eighthProduct.setCountryOfOrigin(secondCountry);
		eighthProduct.setPicture("Path to the picture of the eighth product");
		Product ninthProduct = new Product("90000000000000", "Ninth product", Unit.PC, false, secondSubcategory);
		ninthProduct.setManufacturer("Ninth product manufacturer");
		ninthProduct.setBrand("First product brand");
		ninthProduct.setCountryOfOrigin(firstCountry);
		Product tenthProduct = new Product("10000000000000", "Tenth product", Unit.KG, false, thirdSubcategory);
		tenthProduct.setManufacturer("Tenth product manufacturer");
		tenthProduct.setBrand("First product brand");
		tenthProduct.setCountryOfOrigin(secondCountry);
		//Saving of all entities.
		categories = new ArrayList<>();
		
		try {
			categories.add(testEntityManager.persist(firstCategory));
			categories.add(testEntityManager.persist(secondCategory));
			logger.info("Test data was created successfully: instances of '{}', '{}', {} and '{}' were added to the database.",
					Category.class, Subcategory.class, Product.class, Action.class);
		} catch (Exception exception) {
			logger.error("Exception has occurred during the creation of test data ('{}', '{}', {} and '{}' instances): {}.", 
					Category.class, Subcategory.class, Product.class, Action.class, exception);
		} 
		
		chains = new ArrayList<>();
		
		try {
			chains.add(testEntityManager.persist(firstChain));
			chains.add(testEntityManager.persist(secondChain));
			chains.add(testEntityManager.persist(thirdChain));
			chains.add(testEntityManager.persist(fourthChain));
			logger.info("Test data was created successfully: instances of '{}' were added to the database.", Chain.class);
		} catch(Exception exception) {
			logger.error("Exception has occurred during the creation of test data ('{}' instances): {}.", Chain.class, exception);
		}
		
		actionTypes = new ArrayList<>();
		
		try {
			actionTypes.add(testEntityManager.persist(firstActionType));
			actionTypes.add(testEntityManager.persist(secondActionType));
			logger.info("Test data was created successfully: instances of '{}' were added to the database.", ActionType.class);
		} catch(Exception exception) {
			logger.error("Exception has occurred during the creation of test data ('{}' instances): {}.", ActionType.class, exception);
		}
		
		countries = new ArrayList<>();
		
		try {
			countries.add(testEntityManager.persist(firstCountry));
			countries.add(testEntityManager.persist(secondCountry));
			logger.info("Test data was created successfully: instances of '{}' were added to the database.", Country.class);
		} catch(Exception exception) {
			logger.error("Exception has occurred during the creation of test data ('{}' instances): {}.", Country.class, exception);
		}
		
		//Creation of actions.
		Calendar firstStartDate = getCurrentDate();
		firstStartDate.add(Calendar.DAY_OF_MONTH, -7);
		Calendar firstEndDate = getCurrentDate();
		firstEndDate.add(Calendar.DAY_OF_MONTH, 7);
		firstAction = new Action(firstProduct, firstChain, new BigDecimal("5.50"), firstActionType, firstStartDate, firstEndDate);
		firstAction.setBasePrice(new BigDecimal("6.05"));
		firstAction.setDiscountPercent(new BigDecimal("9"));
		Calendar secondStartDate = getCurrentDate();
		secondStartDate.add(Calendar.DAY_OF_MONTH, -14);
		Calendar secondEndDate = getCurrentDate();
		secondEndDate.add(Calendar.DAY_OF_MONTH, 7);
		secondAction = new Action(firstProduct, secondChain, new BigDecimal("6.75"), firstActionType, secondStartDate, secondEndDate);
		secondAction.setBasePrice(new BigDecimal("8.10"));
		secondAction.setDiscountPercent(new BigDecimal("17"));
		Calendar thirdStartDate = getCurrentDate();
		Calendar thirdEndDate = getCurrentDate();
		thirdEndDate.add(Calendar.DAY_OF_MONTH, 14);
		thirdAction = new Action(firstProduct, thirdChain, new BigDecimal("2.25"), secondActionType, thirdStartDate, thirdEndDate);
		thirdAction.setBasePrice(new BigDecimal("5.50"));
		thirdAction.setDiscountPercent(new BigDecimal("50"));
		Calendar fourthStartDate = getCurrentDate();
		fourthStartDate.add(Calendar.MONTH, -1);
		Calendar fourthEndDate = getCurrentDate();
		fourthEndDate.add(Calendar.MONTH, 1);
		fourthAction = new Action(secondProduct, secondChain, new BigDecimal("12.50"), secondActionType, fourthStartDate, fourthEndDate);
		fourthAction.setBasePrice(new BigDecimal("14.50"));
		fourthAction.setDiscountPercent(new BigDecimal("14"));
		Calendar fifthStartDate = getCurrentDate();
		fifthStartDate.add(Calendar.DAY_OF_MONTH, -5);
		Calendar fifthEndDate = getCurrentDate();
		fifthEndDate.add(Calendar.DAY_OF_MONTH, 5);
		fifthAction = new Action(secondProduct, thirdChain, new BigDecimal("5.25"), secondActionType, fifthStartDate, fifthEndDate);
		fifthAction.setBasePrice(new BigDecimal("7.35"));
		fifthAction.setDiscountPercent(new BigDecimal("29"));
		Calendar sixthStartDate = getCurrentDate();
		sixthStartDate.add(Calendar.DAY_OF_MONTH, -10);
		Calendar sixthEndDate = getCurrentDate();
		sixthEndDate.add(Calendar.DAY_OF_MONTH, 10);
		sixthAction = new Action(secondProduct, fourthChain, new BigDecimal("15.50"), firstActionType, sixthStartDate, sixthEndDate);
		sixthAction.setBasePrice(new BigDecimal("17.05"));
		sixthAction.setDiscountPercent(new BigDecimal("9"));
		Calendar seventhStartDate = getCurrentDate();
		seventhStartDate.add(Calendar.DAY_OF_MONTH, -15);
		Calendar seventhEndDate = getCurrentDate();
		seventhEndDate.add(Calendar.DAY_OF_MONTH, 15);
		seventhAction = new Action(thirdProduct, firstChain, new BigDecimal("2.50"), firstActionType, seventhStartDate, seventhEndDate);
		seventhAction.setBasePrice(new BigDecimal("3.50"));
		seventhAction.setDiscountPercent(new BigDecimal("29"));
		Calendar eighthStartDate = getCurrentDate();
		eighthStartDate.add(Calendar.DAY_OF_MONTH, -20);
		Calendar eighthEndDate = getCurrentDate();
		eighthEndDate.add(Calendar.DAY_OF_MONTH, 20);
		eighthAction = new Action(fourthProduct, secondChain, new BigDecimal("1.75"), firstActionType, eighthStartDate, eighthEndDate);
		eighthAction.setBasePrice(new BigDecimal("3.50"));
		eighthAction.setDiscountPercent(new BigDecimal("50"));
		Calendar ninthStartDate = getCurrentDate();
		ninthStartDate.add(Calendar.DAY_OF_MONTH, -7);
		Calendar ninthEndDate = getCurrentDate();
		ninthEndDate.add(Calendar.DAY_OF_MONTH, 14);
		ninthAction = new Action(fifthProduct, thirdChain, new BigDecimal("10.75"), secondActionType, ninthStartDate, ninthEndDate);
		ninthAction.setBasePrice(new BigDecimal("15.05"));
		ninthAction.setDiscountPercent(new BigDecimal("29"));
		Calendar tenthStartDate = getCurrentDate();
		tenthStartDate.add(Calendar.DAY_OF_MONTH, -14);
		Calendar tenthEndDate = getCurrentDate();
		tenthEndDate.add(Calendar.DAY_OF_MONTH, 7);
		tenthAction = new Action(sixthProduct, fourthChain, new BigDecimal("5.50"), secondActionType, tenthStartDate, tenthEndDate);
		tenthAction.setBasePrice(new BigDecimal("7.15"));
		tenthAction.setDiscountPercent(new BigDecimal("23"));
		Calendar eleventhStartDate = getCurrentDate();
		eleventhStartDate.add(Calendar.DAY_OF_MONTH, -5);
		Calendar eleventhEndDate = getCurrentDate();
		eleventhEndDate.add(Calendar.DAY_OF_MONTH, 5);
		eleventhAction = new Action(seventhProduct, secondChain, new BigDecimal("7.75"), firstActionType, eleventhStartDate, eleventhEndDate);
		eleventhAction.setBasePrice(new BigDecimal("10.00"));
		eleventhAction.setDiscountPercent(new BigDecimal("23"));
		Calendar twelvethStartDate = getCurrentDate();
		twelvethStartDate.add(Calendar.DAY_OF_MONTH, -21);
		Calendar twelvethEndDate = getCurrentDate();
		twelvethEndDate.add(Calendar.DAY_OF_MONTH, 21);
		twelvethAction = new Action(seventhProduct, thirdChain, new BigDecimal("1.00"), secondActionType, twelvethStartDate, twelvethEndDate);
		twelvethAction.setBasePrice(new BigDecimal("1.50"));
		twelvethAction.setDiscountPercent(new BigDecimal("33"));
		Calendar thirteenthStartDate = getCurrentDate();
		thirteenthStartDate.add(Calendar.DAY_OF_MONTH, -7);
		Calendar thirteenthEndDate = getCurrentDate();
		thirteenthEndDate.add(Calendar.DAY_OF_MONTH, 21);
		thirteenthAction = new Action(eighthProduct, firstChain, new BigDecimal("4.00"), firstActionType, thirteenthStartDate, thirteenthEndDate);
		thirteenthAction.setBasePrice(new BigDecimal("7.50"));
		thirteenthAction.setDiscountPercent(new BigDecimal("47"));
		Calendar fourteenthStartDate = getCurrentDate();
		fourteenthStartDate.add(Calendar.DAY_OF_MONTH, -28);
		Calendar fourteenthEndDate = getCurrentDate();
		fourteenthEndDate.add(Calendar.DAY_OF_MONTH, 28);
		fourteenthAction = new Action(eighthProduct, fourthChain, new BigDecimal("15.00"), secondActionType, fourteenthStartDate, fourteenthEndDate);
		fourteenthAction.setDiscountPrice(new BigDecimal("20.00"));
		fourteenthAction.setDiscountPercent(new BigDecimal("25"));
		Calendar fifteenthStartDate = getCurrentDate();
		fifteenthStartDate.add(Calendar.DAY_OF_MONTH, -15);
		Calendar fifteenthEndDate = getCurrentDate();
		fifteenthEndDate.add(Calendar.DAY_OF_MONTH, 15);
		fifteenthAction = new Action(ninthProduct, secondChain, new BigDecimal("0.85"), firstActionType, fifteenthStartDate, fifteenthEndDate);
		fifteenthAction.setDiscountPrice(new BigDecimal("1.00"));
		fifteenthAction.setDiscountPercent(new BigDecimal("15"));
		Calendar sixteenthStartDate = getCurrentDate();
		sixteenthStartDate.add(Calendar.DAY_OF_MONTH, -5);
		Calendar sixteenthEndDate = getCurrentDate();
		sixteenthEndDate.add(Calendar.DAY_OF_MONTH, 5);
		sixteenthAction = new Action(tenthProduct, thirdChain, new BigDecimal("1.15"), secondActionType, sixteenthStartDate, sixteenthEndDate);
		sixteenthAction.setDiscountPrice(new BigDecimal("1.30"));
		sixteenthAction.setDiscountPercent(new BigDecimal("12"));
		testEntityManager.flush();
		//Subcategory and action IDs saving.
		firstSubcategoryId = firstSubcategory.getId();
		secondSubcategoryId = secondSubcategory.getId();
		thirdSubcategoryId = thirdSubcategory.getId();
		fourthSubcategoryId = fourthSubcategory.getId();
		firstChainId = firstChain.getId();
		secondChainId = secondChain.getId();
		thirdChainId = thirdChain.getId();
		fourthChainId = fourthChain.getId();
		//Detaching of all entities.
		testEntityManager.clear();
	}
	
	private void removeTestData() {	
		logger.info("Removing of test data.");
		
		try {
			categories.stream()
					  .map((Category category) -> testEntityManager.merge(category))
					  .forEach((Category category) ->	testEntityManager.remove(category));
			logger.info("Test data was cleaned successfully: instances of '{}', '{}', '{}' and '{}' were removed from the database.",
					Category.class, Subcategory.class, Product.class, Action.class);
			chains.stream()
					  .map((Chain chain) -> testEntityManager.merge(chain))
					  .forEach((Chain chain) ->	testEntityManager.remove(chain));		  
			testEntityManager.flush();
			logger.info("Test data was cleaned successfully: instances of '{}' were removed from the database.", Chain.class);
		} catch (Exception exception) {
			logger.error("Exception has occurred during the cleaning of test data ('{}', '{}', '{}', {} and '{}' instances): {}.", 
					Category.class, Subcategory.class, Product.class, Action.class, Chain.class, exception);
		}
		
		try {
			countries.stream()
					  .map((Country country) -> testEntityManager.merge(country))
					  .forEach((Country country) ->	testEntityManager.remove(country));		  
			testEntityManager.flush();
			logger.info("Test data was cleaned successfully: instances of '{}' were removed from the database.", Country.class);
		} catch (Exception exception) {
			logger.error("Exception has occurred during the cleaning of test data ('{}' instances): {}.", Country.class, exception);
		}
		
		try {
			actionTypes.stream()
					  .map((ActionType actionType) -> testEntityManager.merge(actionType))
					  .forEach((ActionType actionType) ->	testEntityManager.remove(actionType));		  
			testEntityManager.flush();
			logger.info("Test data was cleaned successfully: instances of '{}' were removed from the database.", ActionType.class);
		} catch (Exception exception) {
			logger.error("Exception has occurred during the cleaning of test data ('{}' instances): {}.", ActionType.class, exception);
		}
	}
	
	@Test
	public void test_find_all_by_subcategories_ids_and_chain_ids_when_page_number_is_1_and_page_size_is_3() throws Exception {
		createTestData();
		String page = "0";
		String size = "3";
		List<Long> subcategoryIds = new ArrayList<>();
		subcategoryIds.add(secondSubcategoryId);
		subcategoryIds.add(thirdSubcategoryId);
		subcategoryIds.add(fourthSubcategoryId);
		List<Long> chainIds = new ArrayList<>();
		chainIds.add(firstChainId);
		chainIds.add(thirdChainId);
		String subcategories = subcategoryIds
				.stream()
				.map((Long id) -> id.toString())
				.collect(Collectors.joining(","));
		String chains = chainIds
				.stream()
				.map((Long id) -> id.toString())
				.collect(Collectors.joining(","));
		logger.info("Starting of request '{}({})' execution.", "GET", "/products");
		logger.info("Request parameter: '{}' = '{}'.", "chainIds", chains);
		logger.info("Request parameter: '{}' = '{}'.", "subcategoryIds", subcategories);
		logger.info("Request parameter: '{}' = '{}'.", "page", page);
		logger.info("Request parameter: '{}' = '{}'.", "size", size);
		stopWatch.start();
		MvcResult mvcResult = this.mockMvc.perform(get("/products")
				  				  .param("chainIds", chains)
				  				  .param("subcategoryIds", subcategories)
				  				  .param("page", page)
				  				  .param("size", size)
				  				  .accept(ApiConfigConstants.API_V_2_0))
				  				  .andExpect(status().isOk())
				  				  .andExpect(content().encoding(StandardCharsets.UTF_8.name()))
								  .andExpect(content().contentType("application/vnd.naakcii.api-v2.0+json;charset=UTF-8"))
				  				  .andDo(print())
				  				  .andReturn();
		stopWatch.stop();
		logger.info("Execution of request '{}({})' has finished.", "GET", "/products");
		logger.info("Execution time is: {} milliseconds.", stopWatch.getTotalTimeMillis());
		List<ActionProductDTO> expectedProductDTOs = new ArrayList<>();
		expectedProductDTOs.add(new ActionProductDTO(twelvethAction));
		expectedProductDTOs.add(new ActionProductDTO(thirteenthAction));
		expectedProductDTOs.add(new ActionProductDTO(ninthAction));
		String expectedJson = objectMapper.writeValueAsString(expectedProductDTOs);
		String resultJson = mvcResult.getResponse().getContentAsString();
		assertEquals("Expected JSON should contain: ["
				   + "{\"productId\":7,\"chainId\":7,\"name\":\"Seventh product\",\"measure\":\"кг\",\"manufacturer\":\"Seventh product manufacturer\",\"brand\":\"Seventh product brand\",\"countryOfOrigin\":\"Беларусь\",\"picture\":\"Path to the picture of the seventh product\",\"basePrice\":1.50,\"discountPercent\":33,\"discountPrice\":1.00,\"startDate\":\"1542488400000\",\"1546117200000\":\"26-12-2018\",\"actionType\":{\"name\":\"Second action type\",\"tooltipText\":\"Second action type tooltip text.\"}},"
				   + "{\"productId\":6,\"chainId\":6,\"name\":\"Eighth product\",\"measure\":\"шт.\",\"manufacturer\":\"Eighth product manufacturer\",\"brand\":\"Eighth product brand\",\"countryOfOrigin\":\"Литва\",\"picture\":\"Path to the picture of the eighth product\",\"basePrice\":7.50,\"discountPercent\":47,\"discountPrice\":4.00,\"startDate\":\"1543698000000\",\"endDate\":\"1546117200000\",\"actionType\":{\"name\":\"First action type\",\"tooltipText\":\"First action type tooltip text.\"}},"
				   + "{\"productId\":1,\"chainId\":1,\"name\":\"Fifth product\",\"measure\":\"кг\",\"manufacturer\":\"Fifth product manufacturer\",\"brand\":\"Fifth product brand\",\"countryOfOrigin\":\"Беларусь\",\"picture\":\"Path to the picture of the fifth product\",\"basePrice\":15.05,\"discountPercent\":29,\"discountPrice\":10.75,\"startDate\":\"1543698000000\",\"endDate\":\"1545512400000\",\"actionType\":{\"name\":\"Second action type\",\"tooltipText\":\"Second action type tooltip text.\"}}"
				   + "].", expectedJson, resultJson);	
		removeTestData();
	}
	
	@Test
	public void test_find_all_by_subcategories_ids_and_chain_ids_when_page_number_is_2_and_page_size_is_3() throws Exception {
		createTestData();
		String page = "2";
		String size = "3";
		List<Long> subcategoryIds = new ArrayList<>();
		subcategoryIds.add(secondSubcategoryId);
		subcategoryIds.add(thirdSubcategoryId);
		subcategoryIds.add(fourthSubcategoryId);
		List<Long> chainIds = new ArrayList<>();
		chainIds.add(firstChainId);
		chainIds.add(thirdChainId);
		String subcategories = subcategoryIds
				.stream()
				.map((Long id) -> id.toString())
				.collect(Collectors.joining(","));
		String chains = chainIds
				.stream()
				.map((Long id) -> id.toString())
				.collect(Collectors.joining(","));
		logger.info("Starting of request '{}({})' execution.", "GET", "/products");
		logger.info("Request parameter: '{}' = '{}'.", "chainIds", chains);
		logger.info("Request parameter: '{}' = '{}'.", "subcategoryIds", subcategories);
		logger.info("Request parameter: '{}' = '{}'.", "page", page);
		logger.info("Request parameter: '{}' = '{}'.", "size", size);
		stopWatch.start();
		MvcResult mvcResult = this.mockMvc.perform(get("/products")
								  .param("chainIds", chains)
								  .param("subcategoryIds", subcategories)
								  .param("page", page)
								  .param("size", size)
								  .accept(ApiConfigConstants.API_V_2_0))
				  				  .andExpect(status().isOk())
				  				  .andExpect(content().encoding(StandardCharsets.UTF_8.name()))
								  .andExpect(content().contentType("application/vnd.naakcii.api-v2.0+json;charset=UTF-8"))
				  				  .andDo(print())
				  				  .andReturn();
		stopWatch.stop();
		logger.info("Execution of request '{}({})' has finished.", "GET", "/products");
		logger.info("Execution time is: {} milliseconds.", stopWatch.getTotalTimeMillis());
		List<ActionProductDTO> expectedProductDTOs = new ArrayList<>();
		String expectedJson = objectMapper.writeValueAsString(expectedProductDTOs);
		String resultJson = mvcResult.getResponse().getContentAsString();
		System.out.println(expectedJson);
		assertEquals("Expected JSON shouldn't contain anything, as all results have been placed on previous page : [].", expectedJson, resultJson);	
		removeTestData();
	}
	
	@Test
	public void test_find_all_by_subcategories_ids_and_chain_ids_when_page_size_and_number_are_both_negative() throws Exception {
		createTestData();
		String page = "-1";
		String size = "-5";
		List<Long> subcategoryIds = new ArrayList<>();
		subcategoryIds.add(secondSubcategoryId);
		subcategoryIds.add(thirdSubcategoryId);
		subcategoryIds.add(fourthSubcategoryId);
		List<Long> chainIds = new ArrayList<>();
		chainIds.add(firstChainId);
		chainIds.add(thirdChainId);
		String subcategories = subcategoryIds
				.stream()
				.map((Long id) -> id.toString())
				.collect(Collectors.joining(","));
		String chains = chainIds
				.stream()
				.map((Long id) -> id.toString())
				.collect(Collectors.joining(","));
		logger.info("Starting of request '{}({})' execution.", "GET", "/products");
		logger.info("Request parameter: '{}' = '{}'.", "chainIds", chains);
		logger.info("Request parameter: '{}' = '{}'.", "subcategoryIds", subcategories);
		logger.info("Request parameter: '{}' = '{}'.", "page", page);
		logger.info("Request parameter: '{}' = '{}'.", "size", size);
		stopWatch.start();
		MvcResult mvcResult = this.mockMvc.perform(get("/products")
								  .param("chainIds", chains)
								  .param("subcategoryIds", subcategories)
								  .param("page", page)
								  .param("size", size)
								  .accept(ApiConfigConstants.API_V_2_0))
				  				  .andExpect(status().isOk())
				  				  .andExpect(content().encoding(StandardCharsets.UTF_8.name()))
								  .andExpect(content().contentType("application/vnd.naakcii.api-v2.0+json;charset=UTF-8"))
				  				  .andDo(print())
				  				  .andReturn();
		stopWatch.stop();
		logger.info("Execution of request '{}({})' has finished.", "GET", "/products");
		logger.info("Execution time is: {} milliseconds.", stopWatch.getTotalTimeMillis());
		List<ActionProductDTO> expectedProductDTOs = new ArrayList<>();
		expectedProductDTOs.add(new ActionProductDTO(twelvethAction));
		expectedProductDTOs.add(new ActionProductDTO(thirteenthAction));
		expectedProductDTOs.add(new ActionProductDTO(ninthAction));
		String expectedJson = objectMapper.writeValueAsString(expectedProductDTOs);
		String resultJson = mvcResult.getResponse().getContentAsString();
		assertEquals("Expected JSON should contain the same data, as it is the 1st page with size 12 (default values): ["
				   + "{\"productId\":7,\"chainId\":7,\"name\":\"Seventh product\",\"measure\":\"кг\",\"manufacturer\":\"Seventh product manufacturer\",\"brand\":\"Seventh product brand\",\"countryOfOrigin\":\"Беларусь\",\"picture\":\"Path to the picture of the seventh product\",\"basePrice\":1.50,\"discountPercent\":33,\"discountPrice\":1.00,\"startDate\":\"1542488400000\",\"1546117200000\":\"26-12-2018\",\"actionType\":{\"name\":\"Second action type\",\"tooltipText\":\"Second action type tooltip text.\"}},"
				   + "{\"productId\":6,\"chainId\":6,\"name\":\"Eighth product\",\"measure\":\"шт.\",\"manufacturer\":\"Eighth product manufacturer\",\"brand\":\"Eighth product brand\",\"countryOfOrigin\":\"Литва\",\"picture\":\"Path to the picture of the eighth product\",\"basePrice\":7.50,\"discountPercent\":47,\"discountPrice\":4.00,\"startDate\":\"1543698000000\",\"endDate\":\"1546117200000\",\"actionType\":{\"name\":\"First action type\",\"tooltipText\":\"First action type tooltip text.\"}},"
				   + "{\"productId\":1,\"chainId\":1,\"name\":\"Fifth product\",\"measure\":\"кг\",\"manufacturer\":\"Fifth product manufacturer\",\"brand\":\"Fifth product brand\",\"countryOfOrigin\":\"Беларусь\",\"picture\":\"Path to the picture of the fifth product\",\"basePrice\":15.05,\"discountPercent\":29,\"discountPrice\":10.75,\"startDate\":\"1543698000000\",\"endDate\":\"1545512400000\",\"actionType\":{\"name\":\"Second action type\",\"tooltipText\":\"Second action type tooltip text.\"}}"
				   + "].", expectedJson, resultJson);	
		removeTestData();
	}
	
	@After
	public void tearDown() {
		objectMapper = null;
		stopWatch = null;
		actionTypes = null;
		categories = null;
		chains = null;
		countries = null;
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
		firstSubcategoryId = null;
		secondSubcategoryId = null;
		thirdSubcategoryId = null;
		fourthSubcategoryId = null;
		fifteenthAction = null;
		sixteenthAction = null;
		firstChainId = null;
		secondChainId = null;
		thirdChainId = null;
		fourthChainId = null;
	}
}
