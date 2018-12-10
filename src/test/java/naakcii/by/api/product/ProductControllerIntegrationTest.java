package naakcii.by.api.product;

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
import naakcii.by.api.config.ApiConfigConstants;
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
		firstActionType.setTooltip("First action type tooltip text.");
		ActionType secondActionType = new ActionType("Second action type");
		secondActionType.setTooltip("Second action type tooltip text.");
		//Creation of products.
		Product firstProduct = new Product("10000000000000", "First product", true, firstSubcategory);
		firstProduct.setUnit(Unit.KG);
		firstProduct.setManufacturer("First product manufacturer");
		firstProduct.setBrand("First product brand");
		firstProduct.setCountryOfOrigin("Belarus");
		firstProduct.setPicture("Path to the picture of the first product");
		Product secondProduct = new Product("20000000000000", "Second product", true, firstSubcategory);
		secondProduct.setUnit(Unit.PC);
		secondProduct.setManufacturer("Second product manufacturer");
		secondProduct.setBrand("First product brand");
		secondProduct.setCountryOfOrigin("Belarus");
		secondProduct.setPicture("Path to the picture of the second product");
		Product thirdProduct = new Product("30000000000000", "Third product", true, firstSubcategory);
		thirdProduct.setUnit(Unit.KG);
		thirdProduct.setManufacturer("Third product manufacturer");
		thirdProduct.setBrand("First product brand");
		thirdProduct.setCountryOfOrigin("Belarus");
		thirdProduct.setPicture("Path to the picture of the third product");
		Product fourthProduct = new Product("40000000000000", "Fourth product", true, secondSubcategory);
		fourthProduct.setUnit(Unit.PC);
		fourthProduct.setManufacturer("Fourth product manufacturer");
		fourthProduct.setBrand("First product brand");
		fourthProduct.setCountryOfOrigin("Belarus");
		fourthProduct.setPicture("Path to the picture of the fourth product");
		Product fifthProduct = new Product("50000000000000", "Fifth product", true, secondSubcategory);
		fifthProduct.setUnit(Unit.KG);
		fifthProduct.setManufacturer("Fifth product manufacturer");
		fifthProduct.setBrand("First product brand");
		fifthProduct.setCountryOfOrigin("Belarus");
		fifthProduct.setPicture("Path to the picture of the fifth product");
		Product sixthProduct = new Product("60000000000000", "Sixth product", true, thirdSubcategory);
		sixthProduct.setUnit(Unit.PC);
		sixthProduct.setManufacturer("Sixth product manufacturer");
		sixthProduct.setBrand("First product brand");
		sixthProduct.setCountryOfOrigin("Russia");
		sixthProduct.setPicture("Path to the picture of the sixth product");
		Product seventhProduct = new Product("70000000000000", "Seventh product", true, thirdSubcategory);
		seventhProduct.setUnit(Unit.KG);
		seventhProduct.setManufacturer("Seventh product manufacturer");
		seventhProduct.setBrand("First product brand");
		seventhProduct.setCountryOfOrigin("Latvia");
		seventhProduct.setPicture("Path to the picture of the seventh product");
		Product eighthProduct = new Product("80000000000000", "Eighth product", true, fourthSubcategory);
		eighthProduct.setUnit(Unit.PC);
		eighthProduct.setManufacturer("Eighth product manufacturer");
		eighthProduct.setBrand("First product brand");
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
		List<ProductDTO> expectedProductDTOs = new ArrayList<>();
		expectedProductDTOs.add(new ProductDTO(twelvethAction));
		expectedProductDTOs.add(new ProductDTO(thirteenthAction));
		expectedProductDTOs.add(new ProductDTO(ninthAction));
		String expectedJson = objectMapper.writeValueAsString(expectedProductDTOs);
		String resultJson = mvcResult.getResponse().getContentAsString();
		assertEquals("Expected json should contain: ["
				   + "{\"productId\":7,\"chainId\":7,\"name\":\"Seventh product\",\"measure\":\"кг\",\"manufacturer\":\"Seventh product manufacturer\",\"brand\":\"Seventh product brand\",\"countryOfOrigin\":\"Latvia\",\"picture\":\"Path to the picture of the seventh product\",\"basePrice\":1.50,\"discountPercent\":33,\"discountPrice\":1.00,\"startDate\":\"1542488400000\",\"1546117200000\":\"26-12-2018\",\"actionType\":{\"name\":\"Second action type\",\"tooltipText\":\"Second action type tooltip text.\"}},"
				   + "{\"productId\":6,\"chainId\":6,\"name\":\"Eighth product\",\"measure\":\"шт.\",\"manufacturer\":\"Eighth product manufacturer\",\"brand\":\"Eighth product brand\",\"countryOfOrigin\":\"Poland\",\"picture\":\"Path to the picture of the eighth product\",\"basePrice\":7.50,\"discountPercent\":47,\"discountPrice\":4.00,\"startDate\":\"1543698000000\",\"endDate\":\"1546117200000\",\"actionType\":{\"name\":\"First action type\",\"tooltipText\":\"First action type tooltip text.\"}},"
				   + "{\"productId\":1,\"chainId\":1,\"name\":\"Fifth product\",\"measure\":\"кг\",\"manufacturer\":\"Fifth product manufacturer\",\"brand\":\"Fifth product brand\",\"countryOfOrigin\":\"Belarus\",\"picture\":\"Path to the picture of the fifth product\",\"basePrice\":15.05,\"discountPercent\":29,\"discountPrice\":10.75,\"startDate\":\"1543698000000\",\"endDate\":\"1545512400000\",\"actionType\":{\"name\":\"Second action type\",\"tooltipText\":\"Second action type tooltip text.\"}}"
				   + "].", expectedJson, resultJson);	
	}
	
	@Test
	public void test_find_all_by_subcategories_ids_and_chain_ids_when_page_number_is_2_and_page_size_is_3() throws Exception {
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
		List<ProductDTO> expectedProductDTOs = new ArrayList<>();
		String expectedJson = objectMapper.writeValueAsString(expectedProductDTOs);
		String resultJson = mvcResult.getResponse().getContentAsString();
		System.out.println(expectedJson);
		assertEquals("Expected json shouldn't contain anything, as all results have been placed on previos page : [].", expectedJson, resultJson);	
	}
	
	@Test
	public void test_find_all_by_subcategories_ids_and_chain_ids_when_page_size_and_number_are_both_negative() throws Exception {
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
		List<ProductDTO> expectedProductDTOs = new ArrayList<>();
		expectedProductDTOs.add(new ProductDTO(twelvethAction));
		expectedProductDTOs.add(new ProductDTO(thirteenthAction));
		expectedProductDTOs.add(new ProductDTO(ninthAction));
		String expectedJson = objectMapper.writeValueAsString(expectedProductDTOs);
		String resultJson = mvcResult.getResponse().getContentAsString();
		assertEquals("Expected json should contain the same data, as it is the 1st page with size 12 (default values): ["
				   + "{\"productId\":7,\"chainId\":7,\"name\":\"Seventh product\",\"measure\":\"кг\",\"manufacturer\":\"Seventh product manufacturer\",\"brand\":\"Seventh product brand\",\"countryOfOrigin\":\"Latvia\",\"picture\":\"Path to the picture of the seventh product\",\"basePrice\":1.50,\"discountPercent\":33,\"discountPrice\":1.00,\"startDate\":\"1542488400000\",\"1546117200000\":\"26-12-2018\",\"actionType\":{\"name\":\"Second action type\",\"tooltipText\":\"Second action type tooltip text.\"}},"
				   + "{\"productId\":6,\"chainId\":6,\"name\":\"Eighth product\",\"measure\":\"шт.\",\"manufacturer\":\"Eighth product manufacturer\",\"brand\":\"Eighth product brand\",\"countryOfOrigin\":\"Poland\",\"picture\":\"Path to the picture of the eighth product\",\"basePrice\":7.50,\"discountPercent\":47,\"discountPrice\":4.00,\"startDate\":\"1543698000000\",\"endDate\":\"1546117200000\",\"actionType\":{\"name\":\"First action type\",\"tooltipText\":\"First action type tooltip text.\"}},"
				   + "{\"productId\":1,\"chainId\":1,\"name\":\"Fifth product\",\"measure\":\"кг\",\"manufacturer\":\"Fifth product manufacturer\",\"brand\":\"Fifth product brand\",\"countryOfOrigin\":\"Belarus\",\"picture\":\"Path to the picture of the fifth product\",\"basePrice\":15.05,\"discountPercent\":29,\"discountPrice\":10.75,\"startDate\":\"1543698000000\",\"endDate\":\"1545512400000\",\"actionType\":{\"name\":\"Second action type\",\"tooltipText\":\"Second action type tooltip text.\"}}"
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
