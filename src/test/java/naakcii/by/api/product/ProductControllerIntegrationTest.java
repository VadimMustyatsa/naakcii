package naakcii.by.api.product;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

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
import naakcii.by.api.action.Action;
import naakcii.by.api.actiontype.ActionType;
import naakcii.by.api.category.Category;
import naakcii.by.api.chain.Chain;
import naakcii.by.api.subcategory.Subcategory;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = APIApplication.class)
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private TestEntityManager testEntityManager;
	
	private ObjectMapper objectMapper;
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
		firstActionType.setIcon("First action type icon");
		ActionType secondActionType = new ActionType("Second action type");
		secondActionType.setIcon("Second action type icon");
		//Creation of products.
		Product firstProduct = new Product("10000000000000", "First product", true, firstSubcategory);
		firstProduct.setQuantity(new BigDecimal("250"));
		firstProduct.setMeasure("g");
		firstProduct.setManufacturer("First product manufacturer");
		firstProduct.setCountryOfOrigin("Belarus");
		firstProduct.setPicture("Path to the picture of the first product");
		Product secondProduct = new Product("20000000000000", "Second product", true, firstSubcategory);
		secondProduct.setQuantity(new BigDecimal("500"));
		secondProduct.setMeasure("ml");
		secondProduct.setManufacturer("Second product manufacturer");
		secondProduct.setCountryOfOrigin("Belarus");
		secondProduct.setPicture("Path to the picture of the second product");
		Product thirdProduct = new Product("30000000000000", "Third product", true, firstSubcategory);
		thirdProduct.setQuantity(new BigDecimal("950"));
		thirdProduct.setMeasure("ml");
		thirdProduct.setManufacturer("Third product manufacturer");
		thirdProduct.setCountryOfOrigin("Belarus");
		thirdProduct.setPicture("Path to the picture of the third product");
		Product fourthProduct = new Product("40000000000000", "Fourth product", true, secondSubcategory);
		fourthProduct.setQuantity(new BigDecimal("1"));
		fourthProduct.setMeasure("kg");
		fourthProduct.setManufacturer("Fourth product manufacturer");
		fourthProduct.setCountryOfOrigin("Belarus");
		fourthProduct.setPicture("Path to the picture of the fourth product");
		Product fifthProduct = new Product("50000000000000", "Fifth product", true, secondSubcategory);
		fifthProduct.setQuantity(new BigDecimal("2"));
		fifthProduct.setMeasure("l");
		fifthProduct.setManufacturer("Fifth product manufacturer");
		fifthProduct.setCountryOfOrigin("Belarus");
		fifthProduct.setPicture("Path to the picture of the fifth product");
		Product sixthProduct = new Product("60000000000000", "Sixth product", true, thirdSubcategory);
		sixthProduct.setQuantity(new BigDecimal("0.500"));
		sixthProduct.setMeasure("kg");
		sixthProduct.setManufacturer("Sixth product manufacturer");
		sixthProduct.setCountryOfOrigin("Russia");
		sixthProduct.setPicture("Path to the picture of the sixth product");
		Product seventhProduct = new Product("70000000000000", "Seventh product", true, thirdSubcategory);
		seventhProduct.setQuantity(new BigDecimal("200"));
		seventhProduct.setMeasure("g");
		seventhProduct.setManufacturer("Seventh product manufacturer");
		seventhProduct.setCountryOfOrigin("Latvia");
		seventhProduct.setPicture("Path to the picture of the seventh product");
		Product eighthProduct = new Product("80000000000000", "Eighth product", true, fourthSubcategory);
		eighthProduct.setQuantity(new BigDecimal("2.5"));
		eighthProduct.setMeasure("kg");
		eighthProduct.setManufacturer("Eighth product manufacturer");
		eighthProduct.setCountryOfOrigin("Poland");
		eighthProduct.setPicture("Path to the picture of the eighth product");
		//Saving of all entities.
		testEntityManager.persist(firstCategory);
		testEntityManager.persist(secondCategory);
		testEntityManager.persist(firstChain);
		testEntityManager.persist(secondChain);
		testEntityManager.persist(thirdChain);
		testEntityManager.persist(fourthChain);
		testEntityManager.persist(firstActionType);
		testEntityManager.persist(secondActionType);
		//Creation of actions.
		Calendar firstStartDate = getCurrentDate();
		firstStartDate.add(Calendar.DAY_OF_MONTH, -7);
		Calendar firstEndDate = getCurrentDate();
		firstEndDate.add(Calendar.DAY_OF_MONTH, 7);
		firstAction = new Action(firstProduct, firstChain, new BigDecimal("5.50"), firstActionType, firstStartDate, firstEndDate);
		firstAction.setPrice(new BigDecimal("6.05"));
		firstAction.setDiscount(new BigDecimal("9"));
		Calendar secondStartDate = getCurrentDate();
		secondStartDate.add(Calendar.DAY_OF_MONTH, -14);
		Calendar secondEndDate = getCurrentDate();
		secondEndDate.add(Calendar.DAY_OF_MONTH, 7);
		secondAction = new Action(firstProduct, secondChain, new BigDecimal("6.75"), firstActionType, secondStartDate, secondEndDate);
		secondAction.setPrice(new BigDecimal("8.10"));
		secondAction.setDiscount(new BigDecimal("17"));
		Calendar thirdStartDate = getCurrentDate();
		Calendar thirdEndDate = getCurrentDate();
		thirdEndDate.add(Calendar.DAY_OF_MONTH, 14);
		thirdAction = new Action(firstProduct, thirdChain, new BigDecimal("2.25"), secondActionType, thirdStartDate, thirdEndDate);
		thirdAction.setPrice(new BigDecimal("5.50"));
		thirdAction.setDiscount(new BigDecimal("50"));
		Calendar fourthStartDate = getCurrentDate();
		fourthStartDate.add(Calendar.MONTH, -1);
		Calendar fourthEndDate = getCurrentDate();
		fourthEndDate.add(Calendar.MONTH, 1);
		fourthAction = new Action(secondProduct, secondChain, new BigDecimal("12.50"), secondActionType, fourthStartDate, fourthEndDate);
		fourthAction.setPrice(new BigDecimal("14.50"));
		fourthAction.setDiscount(new BigDecimal("14"));
		Calendar fifthStartDate = getCurrentDate();
		fifthStartDate.add(Calendar.DAY_OF_MONTH, -5);
		Calendar fifthEndDate = getCurrentDate();
		fifthEndDate.add(Calendar.DAY_OF_MONTH, 5);
		fifthAction = new Action(secondProduct, thirdChain, new BigDecimal("5.25"), secondActionType, fifthStartDate, fifthEndDate);
		fifthAction.setPrice(new BigDecimal("7.35"));
		fifthAction.setDiscount(new BigDecimal("29"));
		Calendar sixthStartDate = getCurrentDate();
		sixthStartDate.add(Calendar.DAY_OF_MONTH, -10);
		Calendar sixthEndDate = getCurrentDate();
		sixthEndDate.add(Calendar.DAY_OF_MONTH, 10);
		sixthAction = new Action(secondProduct, fourthChain, new BigDecimal("15.50"), firstActionType, sixthStartDate, sixthEndDate);
		sixthAction.setPrice(new BigDecimal("17.05"));
		sixthAction.setDiscount(new BigDecimal("9"));
		Calendar seventhStartDate = getCurrentDate();
		seventhStartDate.add(Calendar.DAY_OF_MONTH, -15);
		Calendar seventhEndDate = getCurrentDate();
		seventhEndDate.add(Calendar.DAY_OF_MONTH, 15);
		seventhAction = new Action(thirdProduct, firstChain, new BigDecimal("2.50"), firstActionType, seventhStartDate, seventhEndDate);
		seventhAction.setPrice(new BigDecimal("3.50"));
		seventhAction.setDiscount(new BigDecimal("29"));
		Calendar eighthStartDate = getCurrentDate();
		eighthStartDate.add(Calendar.DAY_OF_MONTH, -20);
		Calendar eighthEndDate = getCurrentDate();
		eighthEndDate.add(Calendar.DAY_OF_MONTH, 20);
		eighthAction = new Action(fourthProduct, secondChain, new BigDecimal("1.75"), firstActionType, eighthStartDate, eighthEndDate);
		eighthAction.setPrice(new BigDecimal("3.50"));
		eighthAction.setDiscount(new BigDecimal("50"));
		Calendar ninthStartDate = getCurrentDate();
		ninthStartDate.add(Calendar.DAY_OF_MONTH, -7);
		Calendar ninthEndDate = getCurrentDate();
		ninthEndDate.add(Calendar.DAY_OF_MONTH, 14);
		ninthAction = new Action(fifthProduct, thirdChain, new BigDecimal("10.75"), secondActionType, ninthStartDate, ninthEndDate);
		ninthAction.setPrice(new BigDecimal("15.05"));
		ninthAction.setDiscount(new BigDecimal("29"));
		Calendar tenthStartDate = getCurrentDate();
		tenthStartDate.add(Calendar.DAY_OF_MONTH, -14);
		Calendar tenthEndDate = getCurrentDate();
		tenthEndDate.add(Calendar.DAY_OF_MONTH, 7);
		tenthAction = new Action(sixthProduct, fourthChain, new BigDecimal("5.50"), secondActionType, tenthStartDate, tenthEndDate);
		tenthAction.setPrice(new BigDecimal("7.15"));
		tenthAction.setDiscount(new BigDecimal("23"));
		Calendar eleventhStartDate = getCurrentDate();
		eleventhStartDate.add(Calendar.DAY_OF_MONTH, -5);
		Calendar eleventhEndDate = getCurrentDate();
		eleventhEndDate.add(Calendar.DAY_OF_MONTH, 5);
		eleventhAction = new Action(seventhProduct, secondChain, new BigDecimal("7.75"), firstActionType, eleventhStartDate, eleventhEndDate);
		eleventhAction.setPrice(new BigDecimal("10.00"));
		eleventhAction.setDiscount(new BigDecimal("23"));
		Calendar twelvethStartDate = getCurrentDate();
		twelvethStartDate.add(Calendar.DAY_OF_MONTH, -21);
		Calendar twelvethEndDate = getCurrentDate();
		twelvethEndDate.add(Calendar.DAY_OF_MONTH, 21);
		twelvethAction = new Action(seventhProduct, thirdChain, new BigDecimal("1.00"), secondActionType, twelvethStartDate, twelvethEndDate);
		twelvethAction.setPrice(new BigDecimal("1.50"));
		twelvethAction.setDiscount(new BigDecimal("33"));
		Calendar thirteenthStartDate = getCurrentDate();
		thirteenthStartDate.add(Calendar.DAY_OF_MONTH, -7);
		Calendar thirteenthEndDate = getCurrentDate();
		thirteenthEndDate.add(Calendar.DAY_OF_MONTH, 21);
		thirteenthAction = new Action(eighthProduct, firstChain, new BigDecimal("4.00"), firstActionType, thirteenthStartDate, thirteenthEndDate);
		thirteenthAction.setPrice(new BigDecimal("7.50"));
		thirteenthAction.setDiscount(new BigDecimal("47"));
		Calendar fourteenthStartDate = getCurrentDate();
		fourteenthStartDate.add(Calendar.DAY_OF_MONTH, -28);
		Calendar fourteenthEndDate = getCurrentDate();
		fourteenthEndDate.add(Calendar.DAY_OF_MONTH, 28);
		fourteenthAction = new Action(eighthProduct, fourthChain, new BigDecimal("15.00"), secondActionType, fourteenthStartDate, fourteenthEndDate);
		fourteenthAction.setDiscountPrice(new BigDecimal("20.00"));
		fourteenthAction.setDiscount(new BigDecimal("25"));
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
		testEntityManager.detach(firstActionType);
		testEntityManager.detach(secondActionType);
		testEntityManager.detach(firstChain);
		testEntityManager.detach(secondChain);
		testEntityManager.detach(thirdChain);
		testEntityManager.detach(fourthChain);
		testEntityManager.detach(firstCategory);
		testEntityManager.detach(secondCategory);
	}
	
	@Test
	public void test_find_all_by_subcategories_ids_and_chain_ids_when_page_number_is_1_and_page_size_is_3() throws Exception {
		int page = 0;
		int size = 3;
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
		MvcResult mvcResult = this.mockMvc.perform(get("/products/{chainSet}/{subcategorySet}/{page}/{size}", chains, subcategories, page, size))
				  .andExpect(status().isOk())
				  .andExpect(content().contentType("application/json;charset=UTF-8"))
				  .andDo(print())
				  .andReturn();
		List<ProductDTO> expectedProductDTOs = new ArrayList<>();
		expectedProductDTOs.add(new ProductDTO(twelvethAction));
		expectedProductDTOs.add(new ProductDTO(thirteenthAction));
		expectedProductDTOs.add(new ProductDTO(ninthAction));
		String expectedJson = objectMapper.writeValueAsString(expectedProductDTOs);
		String resultJson = mvcResult.getResponse().getContentAsString();
		assertEquals("Expected json should contain: ["
				   + "{\"productId\":7,\"chainId\":7,\"name\":\"Seventh product\",\"quantity\":200.0,\"measure\":\"g\",\"manufacturer\":\"Seventh product manufacturer\",\"countryOfOrigin\":\"Latvia\",\"picture\":\"Path to the picture of the seventh product\",\"price\":1.50,\"discount\":33,\"discountPrice\":1.00,\"startDate\":\"14-11-2018\",\"endDate\":\"26-12-2018\",\"actionType\":{\"name\":\"Second action type\",\"icon\":\"Second action type icon\"}},"
				   + "{\"productId\":6,\"chainId\":6,\"name\":\"Eighth product\",\"quantity\":2.5,\"measure\":\"kg\",\"manufacturer\":\"Eighth product manufacturer\",\"countryOfOrigin\":\"Poland\",\"picture\":\"Path to the picture of the eighth product\",\"price\":7.50,\"discount\":47,\"discountPrice\":4.00,\"startDate\":\"28-11-2018\",\"endDate\":\"26-12-2018\",\"actionType\":{\"name\":\"First action type\",\"icon\":\"First action type icon\"}},"
				   + "{\"productId\":1,\"chainId\":1,\"name\":\"Fifth product\",\"quantity\":2.0,\"measure\":\"l\",\"manufacturer\":\"Fifth product manufacturer\",\"countryOfOrigin\":\"Belarus\",\"picture\":\"Path to the picture of the fifth product\",\"price\":15.05,\"discount\":29,\"discountPrice\":10.75,\"startDate\":\"28-11-2018\",\"endDate\":\"19-12-2018\",\"actionType\":{\"name\":\"Second action type\",\"icon\":\"Second action type icon\"}}"
				   + "].", expectedJson, resultJson);	
	}
	
	@Test
	public void test_find_all_by_subcategories_ids_and_chain_ids_when_page_number_is_2_and_page_size_is_3() throws Exception {
		int page = 2;
		int size = 3;
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
		MvcResult mvcResult = this.mockMvc.perform(get("/products/{chainSet}/{subcategorySet}/{page}/{size}", chains, subcategories, page, size))
				  .andExpect(status().isOk())
				  .andExpect(content().contentType("application/json;charset=UTF-8"))
				  .andDo(print())
				  .andReturn();
		List<ProductDTO> expectedProductDTOs = new ArrayList<>();
		String expectedJson = objectMapper.writeValueAsString(expectedProductDTOs);
		String resultJson = mvcResult.getResponse().getContentAsString();
		System.out.println(expectedJson);
		assertEquals("Expected json shouldn't contain anything, as all results have been placed on previos page : [].", expectedJson, resultJson);	
	}
	
	@Test
	public void test_find_all_by_subcategories_ids_and_chain_ids_when_page_size_and_number_are_both_negative() throws Exception {
		int page = -1;
		int size = -5;
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
		MvcResult mvcResult = this.mockMvc.perform(get("/products/{chainSet}/{subcategorySet}/{page}/{size}", chains, subcategories, page, size))
				  .andExpect(status().isOk())
				  .andExpect(content().contentType("application/json;charset=UTF-8"))
				  .andDo(print())
				  .andReturn();
		List<ProductDTO> expectedProductDTOs = new ArrayList<>();
		expectedProductDTOs.add(new ProductDTO(twelvethAction));
		expectedProductDTOs.add(new ProductDTO(thirteenthAction));
		expectedProductDTOs.add(new ProductDTO(ninthAction));
		String expectedJson = objectMapper.writeValueAsString(expectedProductDTOs);
		String resultJson = mvcResult.getResponse().getContentAsString();
		assertEquals("Expected json should contain the same data, as it is the 1st page with size 12 (default values): ["
				   + "{\"productId\":7,\"chainId\":7,\"name\":\"Seventh product\",\"quantity\":200.0,\"measure\":\"g\",\"manufacturer\":\"Seventh product manufacturer\",\"countryOfOrigin\":\"Latvia\",\"picture\":\"Path to the picture of the seventh product\",\"price\":1.50,\"discount\":33,\"discountPrice\":1.00,\"startDate\":\"14-11-2018\",\"endDate\":\"26-12-2018\",\"actionType\":{\"name\":\"Second action type\",\"icon\":\"Second action type icon\"}},"
				   + "{\"productId\":6,\"chainId\":6,\"name\":\"Eighth product\",\"quantity\":2.5,\"measure\":\"kg\",\"manufacturer\":\"Eighth product manufacturer\",\"countryOfOrigin\":\"Poland\",\"picture\":\"Path to the picture of the eighth product\",\"price\":7.50,\"discount\":47,\"discountPrice\":4.00,\"startDate\":\"28-11-2018\",\"endDate\":\"26-12-2018\",\"actionType\":{\"name\":\"First action type\",\"icon\":\"First action type icon\"}},"
				   + "{\"productId\":1,\"chainId\":1,\"name\":\"Fifth product\",\"quantity\":2.0,\"measure\":\"l\",\"manufacturer\":\"Fifth product manufacturer\",\"countryOfOrigin\":\"Belarus\",\"picture\":\"Path to the picture of the fifth product\",\"price\":15.05,\"discount\":29,\"discountPrice\":10.75,\"startDate\":\"28-11-2018\",\"endDate\":\"19-12-2018\",\"actionType\":{\"name\":\"Second action type\",\"icon\":\"Second action type icon\"}}"
				   + "].", expectedJson, resultJson);	
	}
	
	@After
	public void tearDown() {
		objectMapper = null;
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
		firstChainId = null;
		secondChainId = null;
		thirdChainId = null;
		fourthChainId = null;
	}
}
