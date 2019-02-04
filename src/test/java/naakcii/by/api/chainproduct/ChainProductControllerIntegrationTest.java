package naakcii.by.api.chainproduct;

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

import naakcii.by.api.unitofmeasure.UnitCode;
import naakcii.by.api.unitofmeasure.UnitOfMeasure;
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
import naakcii.by.api.category.Category;
import naakcii.by.api.chain.Chain;
import naakcii.by.api.chainproduct.ChainProductDTO;
import naakcii.by.api.chainproducttype.ChainProductType;
import naakcii.by.api.config.ApiConfigConstants;
import naakcii.by.api.country.Country;
import naakcii.by.api.country.CountryCode;
import naakcii.by.api.product.Product;
import naakcii.by.api.subcategory.Subcategory;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = APIApplication.class)
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
@Transactional
@TestPropertySource(locations = "classpath:application-integration-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ChainProductControllerIntegrationTest {

	private static final Logger logger = LogManager.getLogger(ChainProductControllerIntegrationTest.class);

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private TestEntityManager testEntityManager;
	
	private ObjectMapper objectMapper;
	private StopWatch stopWatch;
	private List<ChainProductType> chainProductTypes;
	private List<Category> categories;
	private List<Chain> chains;
	private List<Country> countries;
	private List<UnitOfMeasure> unitOfMeasures;
	private ChainProduct firstChainProduct;
	private ChainProduct secondChainProduct;
	private ChainProduct thirdChainProduct;
	private ChainProduct fourthChainProduct;
	private ChainProduct fifthChainProduct;
	private ChainProduct sixthChainProduct;
	private ChainProduct seventhChainProduct;
	private ChainProduct eighthChainProduct;
	private ChainProduct ninthChainProduct;
	private ChainProduct tenthChainProduct;
	private ChainProduct eleventhChainProduct;
	private ChainProduct twelvethChainProduct;
	private ChainProduct thirteenthChainProduct;
	private ChainProduct fourteenthChainProduct;
	private ChainProduct fifteenthChainProduct;
	private ChainProduct sixteenthChainProduct;
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
		Category firstCategory = new Category("Мясо и колбасные изделия", true);
		firstCategory.setIcon("Мясо и колбасные изделия.png");
		Category secondCategory = new Category("Молочные продукты, яйца", true);
		secondCategory.setIcon("Молочные продукты, яйца.png");
		//Creation of subcategories.
		Subcategory firstSubcategory = new Subcategory("Говядина", true, firstCategory);
		Subcategory secondSubcategory = new Subcategory("Колбасные изделия", true, firstCategory);
		Subcategory thirdSubcategory = new Subcategory("Масло", true, secondCategory);
		Subcategory fourthSubcategory = new Subcategory("Молоко", true, secondCategory);
		//Creation of chains.
		Chain firstChain = new Chain("Алми", "Almi", "www.almi.by", true);
		Chain secondChain = new Chain("Виталюр", "Vitalur", "www.vitalur.by", true);
		Chain thirdChain = new Chain("Евроопт", "Evroopt", "www.evroopt.by", true);
		Chain fourthChain = new Chain("БелМаркет", "Belmarket", "www.belmarket.by", true);
		//Creation of chainProduct types.
		ChainProductType firstChainProductType = new ChainProductType("Скидка", "discount");
		firstChainProductType.setTooltip("Скидка всплывающее сообщение.");
		ChainProductType secondChainProductType = new ChainProductType("Хорошая цена", "good_price");
		secondChainProductType.setTooltip("Хорошая цена всплывающее сообщение.");
		//Creation of countries.
		Country firstCountry = new Country(CountryCode.BY);
		Country secondCountry = new Country(CountryCode.LT);
		//Creation of Unit of Measure
		UnitOfMeasure firstUnitOfMeasure = new UnitOfMeasure(UnitCode.KG);
		UnitOfMeasure secondUnitOfMeasure = new UnitOfMeasure(UnitCode.PC);
		//Creation of products.
		Product firstProduct = new Product("10000000000000", "Полуфабрикат из говядины", firstUnitOfMeasure, true, firstSubcategory);
		firstProduct.setManufacturer("ОАО Витебский мясокомбинат");
		firstProduct.setBrand("Витебский мясокомбинат");
		firstProduct.setCountryOfOrigin(firstCountry);
		firstProduct.setPicture("beef.png");
		Product secondProduct = new Product("20000000000000", "Котлетное мясо", firstUnitOfMeasure, true, firstSubcategory);
		secondProduct.setManufacturer("ОАО Минский мясокомбинат");
		secondProduct.setBrand("Минский мясокомбинат");
		secondProduct.setCountryOfOrigin(secondCountry);
		secondProduct.setPicture("meet.png");
		Product thirdProduct = new Product("30000000000000", "Тазобедренная часть", firstUnitOfMeasure, true, firstSubcategory);
		thirdProduct.setManufacturer("ОАО Слонимский мясокомбинат");
		thirdProduct.setBrand("Слонимский мясокомбинат");
		thirdProduct.setCountryOfOrigin(firstCountry);
		thirdProduct.setPicture("beef_thigh.png");
		Product fourthProduct = new Product("40000000000000", "Сервелат", firstUnitOfMeasure, true, secondSubcategory);
		fourthProduct.setManufacturer("ОАО Дзержинский мясокомбинат");
		fourthProduct.setBrand("Дзержинский мясокомбинат");
		fourthProduct.setCountryOfOrigin(secondCountry);
		fourthProduct.setPicture("servelat.png");
		Product fifthProduct = new Product("50000000000000", "Салями", secondUnitOfMeasure, true, secondSubcategory);
		fifthProduct.setManufacturer("СООО Старфуд");
		fifthProduct.setBrand("Смакі прысмакі");
		fifthProduct.setCountryOfOrigin(firstCountry);
		fifthProduct.setPicture("salyami.png");
		Product sixthProduct = new Product("60000000000000", "Масло сливочное 82.5%", firstUnitOfMeasure, true, thirdSubcategory);
		sixthProduct.setManufacturer("ОАО Молочные горки");
		sixthProduct.setBrand("Простоквашино");
		sixthProduct.setCountryOfOrigin(secondCountry);
		sixthProduct.setPicture("butter1.png");
		Product seventhProduct = new Product("70000000000000", "Масло особое", firstUnitOfMeasure, true, thirdSubcategory);
		seventhProduct.setManufacturer("ОАО Барановичский мясомолочный комбинат");
		seventhProduct.setBrand("Раніца");
		seventhProduct.setCountryOfOrigin(firstCountry);
		seventhProduct.setPicture("butter2.png");
		Product eighthProduct = new Product("80000000000000", "Молоко 2.5%", secondUnitOfMeasure, true, fourthSubcategory);
		eighthProduct.setManufacturer("ОАО Молочные горки");
		eighthProduct.setBrand("Простоквашино");
		eighthProduct.setCountryOfOrigin(secondCountry);
		eighthProduct.setPicture("milk.png");
		Product ninthProduct = new Product("90000000000000", "Колбаса Купеческая", firstUnitOfMeasure, false, secondSubcategory);
		ninthProduct.setManufacturer("ОАО Слонимский мясокомбинат");
		ninthProduct.setBrand("Слонимский мясокомбинат");
		ninthProduct.setCountryOfOrigin(firstCountry);
		Product tenthProduct = new Product("10000000000000", "Масло сладкосливочное", firstUnitOfMeasure, false, thirdSubcategory);
		tenthProduct.setManufacturer("ОАО Слуцкий сыродельный комбинат");
		tenthProduct.setBrand("Стары Менск");
		tenthProduct.setCountryOfOrigin(secondCountry);
		//Saving of all entities.
		unitOfMeasures = new ArrayList<>();
		try {
			unitOfMeasures.add(testEntityManager.persist(firstUnitOfMeasure));
			unitOfMeasures.add(testEntityManager.persist(secondUnitOfMeasure));
			logger.info("Test data was created successfully: instances of '{}' were added to the database.", UnitOfMeasure.class);
		} catch (Exception exception) {
			logger.error("Exception has occurred during the creation of test data ('{}' instances): {}.", UnitOfMeasure.class, exception);
		}

		categories = new ArrayList<>();
		
		try {
			categories.add(testEntityManager.persist(firstCategory));
			categories.add(testEntityManager.persist(secondCategory));
			logger.info("Test data was created successfully: instances of '{}', '{}', {} and '{}' were added to the database.",
					Category.class, Subcategory.class, Product.class, ChainProduct.class);
		} catch (Exception exception) {
			logger.error("Exception has occurred during the creation of test data ('{}', '{}', {} and '{}' instances): {}.", 
					Category.class, Subcategory.class, Product.class, ChainProduct.class, exception);
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
		
		chainProductTypes = new ArrayList<>();
		
		try {
			chainProductTypes.add(testEntityManager.persist(firstChainProductType));
			chainProductTypes.add(testEntityManager.persist(secondChainProductType));
			logger.info("Test data was created successfully: instances of '{}' were added to the database.", ChainProductType.class);
		} catch(Exception exception) {
			logger.error("Exception has occurred during the creation of test data ('{}' instances): {}.", ChainProductType.class, exception);
		}
		
		countries = new ArrayList<>();
		
		try {
			countries.add(testEntityManager.persist(firstCountry));
			countries.add(testEntityManager.persist(secondCountry));
			logger.info("Test data was created successfully: instances of '{}' were added to the database.", Country.class);
		} catch(Exception exception) {
			logger.error("Exception has occurred during the creation of test data ('{}' instances): {}.", Country.class, exception);
		}
		
		//Creation of chainProducts.
		Calendar firstStartDate = getCurrentDate();
		firstStartDate.add(Calendar.DAY_OF_MONTH, -7);
		Calendar firstEndDate = getCurrentDate();
		firstEndDate.add(Calendar.DAY_OF_MONTH, 7);
		firstChainProduct = new ChainProduct(firstProduct, firstChain, new BigDecimal("5.50"), firstChainProductType, firstStartDate, firstEndDate);
		firstChainProduct.setBasePrice(new BigDecimal("6.05"));
		firstChainProduct.setDiscountPercent(new BigDecimal("9"));
		Calendar secondStartDate = getCurrentDate();
		secondStartDate.add(Calendar.DAY_OF_MONTH, -14);
		Calendar secondEndDate = getCurrentDate();
		secondEndDate.add(Calendar.DAY_OF_MONTH, 7);
		secondChainProduct = new ChainProduct(firstProduct, secondChain, new BigDecimal("6.75"), firstChainProductType, secondStartDate, secondEndDate);
		secondChainProduct.setBasePrice(new BigDecimal("8.10"));
		secondChainProduct.setDiscountPercent(new BigDecimal("17"));
		Calendar thirdStartDate = getCurrentDate();
		Calendar thirdEndDate = getCurrentDate();
		thirdEndDate.add(Calendar.DAY_OF_MONTH, 14);
		thirdChainProduct = new ChainProduct(firstProduct, thirdChain, new BigDecimal("2.25"), secondChainProductType, thirdStartDate, thirdEndDate);
		thirdChainProduct.setBasePrice(new BigDecimal("5.50"));
		thirdChainProduct.setDiscountPercent(new BigDecimal("50"));
		Calendar fourthStartDate = getCurrentDate();
		fourthStartDate.add(Calendar.MONTH, -1);
		Calendar fourthEndDate = getCurrentDate();
		fourthEndDate.add(Calendar.MONTH, 1);
		fourthChainProduct = new ChainProduct(secondProduct, secondChain, new BigDecimal("12.50"), secondChainProductType, fourthStartDate, fourthEndDate);
		fourthChainProduct.setBasePrice(new BigDecimal("14.50"));
		fourthChainProduct.setDiscountPercent(new BigDecimal("14"));
		Calendar fifthStartDate = getCurrentDate();
		fifthStartDate.add(Calendar.DAY_OF_MONTH, -5);
		Calendar fifthEndDate = getCurrentDate();
		fifthEndDate.add(Calendar.DAY_OF_MONTH, 5);
		fifthChainProduct = new ChainProduct(secondProduct, thirdChain, new BigDecimal("5.25"), secondChainProductType, fifthStartDate, fifthEndDate);
		fifthChainProduct.setBasePrice(new BigDecimal("7.35"));
		fifthChainProduct.setDiscountPercent(new BigDecimal("29"));
		Calendar sixthStartDate = getCurrentDate();
		sixthStartDate.add(Calendar.DAY_OF_MONTH, -10);
		Calendar sixthEndDate = getCurrentDate();
		sixthEndDate.add(Calendar.DAY_OF_MONTH, 10);
		sixthChainProduct = new ChainProduct(secondProduct, fourthChain, new BigDecimal("15.50"), firstChainProductType, sixthStartDate, sixthEndDate);
		sixthChainProduct.setBasePrice(new BigDecimal("17.05"));
		sixthChainProduct.setDiscountPercent(new BigDecimal("9"));
		Calendar seventhStartDate = getCurrentDate();
		seventhStartDate.add(Calendar.DAY_OF_MONTH, -15);
		Calendar seventhEndDate = getCurrentDate();
		seventhEndDate.add(Calendar.DAY_OF_MONTH, 15);
		seventhChainProduct = new ChainProduct(thirdProduct, firstChain, new BigDecimal("2.50"), firstChainProductType, seventhStartDate, seventhEndDate);
		seventhChainProduct.setBasePrice(new BigDecimal("3.50"));
		seventhChainProduct.setDiscountPercent(new BigDecimal("29"));
		Calendar eighthStartDate = getCurrentDate();
		eighthStartDate.add(Calendar.DAY_OF_MONTH, -20);
		Calendar eighthEndDate = getCurrentDate();
		eighthEndDate.add(Calendar.DAY_OF_MONTH, 20);
		eighthChainProduct = new ChainProduct(fourthProduct, secondChain, new BigDecimal("1.75"), firstChainProductType, eighthStartDate, eighthEndDate);
		eighthChainProduct.setBasePrice(new BigDecimal("3.50"));
		eighthChainProduct.setDiscountPercent(new BigDecimal("50"));
		Calendar ninthStartDate = getCurrentDate();
		ninthStartDate.add(Calendar.DAY_OF_MONTH, -7);
		Calendar ninthEndDate = getCurrentDate();
		ninthEndDate.add(Calendar.DAY_OF_MONTH, 14);
		ninthChainProduct = new ChainProduct(fifthProduct, thirdChain, new BigDecimal("10.75"), secondChainProductType, ninthStartDate, ninthEndDate);
		ninthChainProduct.setBasePrice(new BigDecimal("15.05"));
		ninthChainProduct.setDiscountPercent(new BigDecimal("29"));
		Calendar tenthStartDate = getCurrentDate();
		tenthStartDate.add(Calendar.DAY_OF_MONTH, -14);
		Calendar tenthEndDate = getCurrentDate();
		tenthEndDate.add(Calendar.DAY_OF_MONTH, 7);
		tenthChainProduct = new ChainProduct(sixthProduct, fourthChain, new BigDecimal("5.50"), secondChainProductType, tenthStartDate, tenthEndDate);
		tenthChainProduct.setBasePrice(new BigDecimal("7.15"));
		tenthChainProduct.setDiscountPercent(new BigDecimal("23"));
		Calendar eleventhStartDate = getCurrentDate();
		eleventhStartDate.add(Calendar.DAY_OF_MONTH, -5);
		Calendar eleventhEndDate = getCurrentDate();
		eleventhEndDate.add(Calendar.DAY_OF_MONTH, 5);
		eleventhChainProduct = new ChainProduct(seventhProduct, secondChain, new BigDecimal("7.75"), firstChainProductType, eleventhStartDate, eleventhEndDate);
		eleventhChainProduct.setBasePrice(new BigDecimal("10.00"));
		eleventhChainProduct.setDiscountPercent(new BigDecimal("23"));
		Calendar twelvethStartDate = getCurrentDate();
		twelvethStartDate.add(Calendar.DAY_OF_MONTH, -21);
		Calendar twelvethEndDate = getCurrentDate();
		twelvethEndDate.add(Calendar.DAY_OF_MONTH, 21);
		twelvethChainProduct = new ChainProduct(seventhProduct, thirdChain, new BigDecimal("1.00"), secondChainProductType, twelvethStartDate, twelvethEndDate);
		twelvethChainProduct.setBasePrice(new BigDecimal("1.50"));
		twelvethChainProduct.setDiscountPercent(new BigDecimal("33"));
		Calendar thirteenthStartDate = getCurrentDate();
		thirteenthStartDate.add(Calendar.DAY_OF_MONTH, -7);
		Calendar thirteenthEndDate = getCurrentDate();
		thirteenthEndDate.add(Calendar.DAY_OF_MONTH, 21);
		thirteenthChainProduct = new ChainProduct(eighthProduct, firstChain, new BigDecimal("4.00"), firstChainProductType, thirteenthStartDate, thirteenthEndDate);
		thirteenthChainProduct.setBasePrice(new BigDecimal("7.50"));
		thirteenthChainProduct.setDiscountPercent(new BigDecimal("47"));
		Calendar fourteenthStartDate = getCurrentDate();
		fourteenthStartDate.add(Calendar.DAY_OF_MONTH, -28);
		Calendar fourteenthEndDate = getCurrentDate();
		fourteenthEndDate.add(Calendar.DAY_OF_MONTH, 28);
		fourteenthChainProduct = new ChainProduct(eighthProduct, fourthChain, new BigDecimal("15.00"), secondChainProductType, fourteenthStartDate, fourteenthEndDate);
		fourteenthChainProduct.setDiscountPrice(new BigDecimal("20.00"));
		fourteenthChainProduct.setDiscountPercent(new BigDecimal("25"));
		Calendar fifteenthStartDate = getCurrentDate();
		fifteenthStartDate.add(Calendar.DAY_OF_MONTH, -15);
		Calendar fifteenthEndDate = getCurrentDate();
		fifteenthEndDate.add(Calendar.DAY_OF_MONTH, 15);
		fifteenthChainProduct = new ChainProduct(ninthProduct, secondChain, new BigDecimal("0.85"), firstChainProductType, fifteenthStartDate, fifteenthEndDate);
		fifteenthChainProduct.setDiscountPrice(new BigDecimal("1.00"));
		fifteenthChainProduct.setDiscountPercent(new BigDecimal("15"));
		Calendar sixteenthStartDate = getCurrentDate();
		sixteenthStartDate.add(Calendar.DAY_OF_MONTH, -5);
		Calendar sixteenthEndDate = getCurrentDate();
		sixteenthEndDate.add(Calendar.DAY_OF_MONTH, 5);
		sixteenthChainProduct = new ChainProduct(tenthProduct, thirdChain, new BigDecimal("1.15"), secondChainProductType, sixteenthStartDate, sixteenthEndDate);
		sixteenthChainProduct.setDiscountPrice(new BigDecimal("1.30"));
		sixteenthChainProduct.setDiscountPercent(new BigDecimal("12"));
		testEntityManager.flush();
		//Subcategory and chainProduct IDs saving.
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
			categories.stream().forEach((Category category) ->	testEntityManager.remove(testEntityManager.merge(category)));
			logger.info("Test data was cleaned successfully: instances of '{}', '{}', '{}' and '{}' were removed from the database.",
					Category.class, Subcategory.class, Product.class, ChainProduct.class);
			chains.stream().forEach((Chain chain) ->	testEntityManager.remove(testEntityManager.merge(chain)));		  
			testEntityManager.flush();
			logger.info("Test data was cleaned successfully: instances of '{}' were removed from the database.", Chain.class);
		} catch (Exception exception) {
			logger.error("Exception has occurred during the cleaning of test data ('{}', '{}', '{}', {} and '{}' instances): {}.", 
					Category.class, Subcategory.class, Product.class, ChainProduct.class, Chain.class, exception);
		}
		
		try {
			countries.stream().forEach((Country country) ->	testEntityManager.remove(testEntityManager.merge(country)));		  
			testEntityManager.flush();
			logger.info("Test data was cleaned successfully: instances of '{}' were removed from the database.", Country.class);
		} catch (Exception exception) {
			logger.error("Exception has occurred during the cleaning of test data ('{}' instances): {}.", Country.class, exception);
		}

		try {
			unitOfMeasures.stream().forEach((UnitOfMeasure unitOfMeasure) ->	testEntityManager.remove(testEntityManager.merge(unitOfMeasure)));
			testEntityManager.flush();
			logger.info("Test data was cleaned successfully: instances of '{}' were removed from the database.", UnitOfMeasure.class);
		} catch (Exception exception) {
			logger.error("Exception has occurred during the cleaning of test data ('{}' instances): {}.", UnitOfMeasure.class, exception);
		}
		
		try {
			chainProductTypes.stream().forEach((ChainProductType chainProductType) ->	testEntityManager.remove(testEntityManager.merge(chainProductType)));
			testEntityManager.flush();
			logger.info("Test data was cleaned successfully: instances of '{}' were removed from the database.", ChainProductType.class);
		} catch (Exception exception) {
			logger.error("Exception has occurred during the cleaning of test data ('{}' instances): {}.", ChainProductType.class, exception);
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
		List<ChainProductDTO> expectedProductDTOs = new ArrayList<>();
		expectedProductDTOs.add(new ChainProductDTO(twelvethChainProduct));
		expectedProductDTOs.add(new ChainProductDTO(thirteenthChainProduct));
		expectedProductDTOs.add(new ChainProductDTO(ninthChainProduct));
		String expectedJson = objectMapper.writeValueAsString(expectedProductDTOs);
		String resultJson = mvcResult.getResponse().getContentAsString();
		assertEquals(expectedJson, resultJson);
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
		List<ChainProductDTO> expectedProductDTOs = new ArrayList<>();
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
		List<ChainProductDTO> expectedProductDTOs = new ArrayList<>();
		expectedProductDTOs.add(new ChainProductDTO(twelvethChainProduct));
		expectedProductDTOs.add(new ChainProductDTO(thirteenthChainProduct));
		expectedProductDTOs.add(new ChainProductDTO(ninthChainProduct));
		String expectedJson = objectMapper.writeValueAsString(expectedProductDTOs);
		String resultJson = mvcResult.getResponse().getContentAsString();
		assertEquals("Expected JSON should contain the same data, as it is the 1st page with size 12 (default values): ["
				   + "{productId:7, chainId:7, name:'Масло особое',unitOfMeasure:{name:'кг', step:0.1}, manufacturer:'ОАО Барановичский мясомолочный комбинат', brand :'Раніца', countryOfOrigin:'Беларусь', picture: 'butter2.png', basePrice:1.50, discountPercent:33, discountPrice:1.00, startDate:1542488400000, endDate:1546117200000, chainProductType:{name: 'Хорошая цена, tooltipText:'Хорошая цена всплывающее сообщение.', synonym:'good_price'}},"
				   + "{productId:6, chainId:6, name:'Молоко 2.5%', unitOfMeasure:{name:'шт', step:1.0}, manufacturer:'ОАО Молочные горки', brand:'Простоквашино', countryOfOrigin:'Литва', picture:'milk.png', basePrice:7.50, discountPercent:47, discountPrice:4.00, startDate:1543698000000, endDate:1546117200000, chainProductType:{name:'Скидка', tooltipText:'Скидка всплывающее сообщение.', synonym:'discount'},"
				   + "{productId:1, chainId:1, name:'Салями', unitOfMeasure:{name:'шт', step:1.0}, manufacturer:'СООО Старфуд', brand:'Смакі прысмакі', countryOfOrigin:'Беларусь', picture:'salyami.png', basePrice:15.05, discountPercent:29, discountPrice:10.75, startDate:1543698000000, endDate:1545512400000, chainProductType:{name:'Хорошая цена', tooltipText:'Хорошая цена всплывающее сообщение.', synonym:'good_price'}}"
				   + "].", expectedJson, resultJson);	
		removeTestData();
	}
	
	@After
	public void tearDown() {
		objectMapper = null;
		stopWatch = null;
		chainProductTypes = null;
		categories = null;
		chains = null;
		countries = null;
		unitOfMeasures = null;
		firstChainProduct = null;
		secondChainProduct = null;
		thirdChainProduct = null;
		fourthChainProduct = null;
		fifthChainProduct = null;
		sixthChainProduct = null;
		seventhChainProduct = null;
		eighthChainProduct = null;
		ninthChainProduct = null;
		tenthChainProduct = null;
		eleventhChainProduct = null;
		twelvethChainProduct = null;
		thirteenthChainProduct = null;
		fourteenthChainProduct = null;
		firstSubcategoryId = null;
		secondSubcategoryId = null;
		thirdSubcategoryId = null;
		fourthSubcategoryId = null;
		fifteenthChainProduct = null;
		sixteenthChainProduct = null;
		firstChainId = null;
		secondChainId = null;
		thirdChainId = null;
		fourthChainId = null;
	}
}
