package naakcii.by.api.util.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.TypedQuery;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import naakcii.by.api.APIApplication;
import naakcii.by.api.category.Category;
import naakcii.by.api.category.CategoryRepository;
import naakcii.by.api.chain.Chain;
import naakcii.by.api.chain.ChainRepository;
import naakcii.by.api.chainproduct.ChainProduct;
import naakcii.by.api.chainproduct.ChainProductRepository;
import naakcii.by.api.chainproducttype.ChainProductType;
import naakcii.by.api.chainproducttype.ChainProductTypeRepository;
import naakcii.by.api.country.Country;
import naakcii.by.api.country.CountryCode;
import naakcii.by.api.country.CountryRepository;
import naakcii.by.api.product.Product;
import naakcii.by.api.product.ProductRepository;
import naakcii.by.api.subcategory.Subcategory;
import naakcii.by.api.subcategory.SubcategoryRepository;
import naakcii.by.api.unitofmeasure.UnitCode;
import naakcii.by.api.unitofmeasure.UnitOfMeasure;
import naakcii.by.api.unitofmeasure.UnitOfMeasureRepository;
import naakcii.by.api.util.ObjectFactory;
import naakcii.by.api.util.parser.DataParserImpl;
import naakcii.by.api.util.parser.ParsingResult.Status;
import naakcii.by.api.util.parser.enumeration.EnumerationParsingResult;
import naakcii.by.api.util.parser.multisheet.MultisheetParsingResult;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = APIApplication.class)
@AutoConfigureTestEntityManager
@Transactional
@TestPropertySource(locations = "classpath:application-integration-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class DataParserImplTest {
	
	private static final Logger logger = LogManager.getLogger(DataParserImplTest.class);
	private static final String FILE_WITH_BASIC_DATA = 
			"src" + File.separator + "test" + File.separator + "resources" + File.separator + "Basic_data.xlsx";
	private static final String FILE_WITH_CHAIN_PRODUCTS = 
			"src" + File.separator + "test" + File.separator + "resources" + File.separator + "Test_chain_products.xlsx";
	private static final String SHEET_WITH_BASIC_CHAIN_PRODUCT_TYPES = "Basic_chain_product_types";
	private static final String SHEET_WITH_BASIC_CATEGORIES = "Basic_categories";
	private static final String SHEET_WITH_BASIC_CHAINS = "Basic_chains";
	private static final String SHEET_WITH_BASIC_SUBCATEGORIES = "Basic_subcategories";
	private static final String SHEET_WITH_CHAIN_PRODUCTS = "Chain_products";
	private static final String INDEFINITE_CATEGORY = "Indefinite category";
	private static final String INDEFINITE_SUBCATEGORY = "Indefinite subcategory";
	
	private DataParserImpl dataParser;
	
	@Autowired
	private TestEntityManager testEntityManager;
	
	@Autowired
	private ObjectFactory objectFactory;
	
	@Autowired
	private ChainRepository chainRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private SubcategoryRepository subcategoryRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ChainProductRepository actionRepository;
	
	@Autowired
	private ChainProductTypeRepository actionTypeRepository;
	
	@Autowired
	private CountryRepository countryRepository;
	
	@Autowired
	private UnitOfMeasureRepository unitOfMeasureRepository;
	
	@Before
	public void setUp() {
		dataParser = new DataParserImpl(objectFactory, chainRepository, categoryRepository, subcategoryRepository, 
										productRepository, actionRepository, actionTypeRepository, countryRepository, 
										unitOfMeasureRepository); 
	}
	
	@Test
	public void test_create_countries() {
		ParsingResult<Country, CountryCode> expectedParsingResult = 
				new EnumerationParsingResult<Country, CountryCode>(Country.class, CountryCode.class);
		expectedParsingResult.setStatus(Status.OK);
		expectedParsingResult.setNumberOfSavedInstances(250);
		expectedParsingResult.setTotalNumberOfInstances(250);
		EnumerationParsingResult<Country, CountryCode> parsingResult = dataParser.createCountries();
		List<Country> expectedCountries = Arrays.stream(CountryCode.values()).map(Country::new).collect(Collectors.toList());
		TypedQuery<Country> getAllCountries = testEntityManager
				.getEntityManager()
				.createQuery("Select country from Country country", Country.class);
		List<Country> resultCountries = getAllCountries.getResultList();
		assertEquals("Result list of countries should contain 250 elements.", 250, resultCountries.size());
		assertTrue("Result list of countries should contain all elements of expected list: ["
				+ "{name:'Австралия', alphaCode2:'AU', alphaCode3:'AUS'},"
				+ "..."
				+ "{name:'Япония', alphaCode2:'JP', alphaCode3:'JPN'}"
				+ "].", resultCountries.containsAll(expectedCountries));
		assertEquals("Parsing result should be:"
				+ "target class - naakcii.by.api.country.Country;"
				+ "source enumeration class - naakcii.by.api.country.CountryCode;"
				+ "status - OK;"
				+ "total number of instances - 250;"
				+ "number of saved instances - 250;"
				+ "number of unsaved instances - 0,"
				+ "including"
				+ "number of already existing instances - 0;"
				+ "number of invalid instances - 0;"
				+ "number of common warnings - 0;"
				+ "number of common exceptions - 0;"
				+ "warnings - 0;"
				+ "constraint violations - 0;"
				+ "exceptions - 0."
				, expectedParsingResult, parsingResult);
		logger.info("Removing of test data.");
		
		try {
			resultCountries.stream().forEach(testEntityManager::remove);
			testEntityManager.flush();
			logger.info("Test data was cleaned successfully: instances of '{}' were removed from the database.", Country.class);
		} catch (Exception exception) {
			logger.error("Exception has occurred during the removing of test data ('{}' instances): {}.", Country.class, exception);
		}
	}
	
	@Test
	public void test_create_units_of_measure() {
		ParsingResult<UnitOfMeasure, UnitCode> expectedParsingResult = 
				new EnumerationParsingResult<UnitOfMeasure, UnitCode>(UnitOfMeasure.class, UnitCode.class);
		expectedParsingResult.setStatus(Status.OK);
		expectedParsingResult.setNumberOfSavedInstances(2);
		expectedParsingResult.setTotalNumberOfInstances(2);
		EnumerationParsingResult<UnitOfMeasure, UnitCode> parsingResult = dataParser.createUnitsOfMeasure();
		List<UnitOfMeasure> expectedUnits = Arrays.stream(UnitCode.values()).map(UnitOfMeasure::new).collect(Collectors.toList());
		TypedQuery<UnitOfMeasure> getAllUnits = testEntityManager
				.getEntityManager()
				.createQuery("Select unit from UnitOfMeasure unit", UnitOfMeasure.class);
		List<UnitOfMeasure> resultUnits = getAllUnits.getResultList();
		assertEquals("Result list of units of measure should contain 2 elements.", 2, resultUnits.size());
		assertTrue("Result list of units of measure should contain all elements of expected list: ["
				+ "{name:'кг', step:0.1},"
				+ "{name:'шт', step:1.0}"
				+ "].", resultUnits.containsAll(expectedUnits));
		assertEquals("Parsing result should be:"
				+ "target class - naakcii.by.api.unitofmeasure.UnitOfMeasure;"
				+ "source enumeration class - naakcii.by.api.unitofmeasure.UnitCode;"
				+ "status - OK;"
				+ "total number of instances - 2;"
				+ "number of saved instances - 2;"
				+ "number of unsaved instances - 0,"
				+ "including"
				+ "number of already existing instances - 0;"
				+ "number of invalid instances - 0;"
				+ "number of common warnings - 0;"
				+ "number of common exceptions - 0;"
				+ "warnings - 0;"
				+ "constraint violations - 0;"
				+ "exceptions - 0."
				, expectedParsingResult, parsingResult);
		logger.info("Removing of test data.");
		
		try {
			resultUnits.stream().forEach(testEntityManager::remove);
			testEntityManager.flush();
			logger.info("Test data was cleaned successfully: instances of '{}' were removed from the database.", UnitOfMeasure.class);
		} catch (Exception exception) {
			logger.error("Exception has occurred during the removing of test data ('{}' instances): {}.", UnitOfMeasure.class, exception);
		}
	}
	
	@Test
	public void test_create_basic_chain_product_types() {
		MultisheetParsingResult<ChainProductType> expectedParsingResult = new MultisheetParsingResult<>(ChainProductType.class, FILE_WITH_BASIC_DATA);
		expectedParsingResult.setSheetName(SHEET_WITH_BASIC_CHAIN_PRODUCT_TYPES);
		expectedParsingResult.setSheetIndex(3);
		expectedParsingResult.setNumberOfSavedInstances(3);
		expectedParsingResult.setTotalNumberOfInstances(3);
		expectedParsingResult.setStatus(Status.OK);
		List<ChainProductType> expectedChainProductTypes = new ArrayList<>();
		expectedChainProductTypes.add(new ChainProductType("Скидка", "discount"));
		expectedChainProductTypes.add(new ChainProductType("Хорошая цена", "good_price"));
		expectedChainProductTypes.add(new ChainProductType("1+1", "one_plus_one"));
		List<ParsingResult<?, ?>> parsingResult = dataParser.parseBasicData(ChainProductType.class);
		TypedQuery<ChainProductType> getAllChainProductTypes = testEntityManager
				.getEntityManager()
				.createQuery("Select type from ChainProductType type", ChainProductType.class);
		List<ChainProductType> resultChainProductTypes = getAllChainProductTypes.getResultList();
		assertEquals("Result list of chain product types should contain 3 elements.", 3, resultChainProductTypes.size());
		assertTrue("Result list of action types should contain all elements of expected list: ["
				+ "{name:'Скидка', tooltip:null, synonym:'discount'}, "
				+ "{name:'Хорошая цена', tooltip:null, synonym:'good_price'}, "
				+ "{name:'1+1', tooltip:null, synonym:'one_plus_one'}"
				+ "].",	resultChainProductTypes.containsAll(expectedChainProductTypes));
		assertEquals("Parsing result should be:"
				+ "target class - naakcii.by.api.chainproducttype.ChainProductType;"
				+ "file - src/test/resources/Basic_data.xlsx;"
				+ "sheet name - Basic_chain_product_types;"
				+ "sheet index - 3;"
				+ "status - OK;"
				+ "total number of instances - 3;"
				+ "number of saved instances - 3;"
				+ "number of unsaved instances - 0,"
				+ "including"
				+ "number of already existing instances - 0;"
				+ "number of invalid instances - 0;"
				+ "number of common warnings - 0;"
				+ "number of common exceptions - 0;"
				+ "warnings - 0;"
				+ "constraint violations - 0;"
				+ "exceptions - 0."
				, expectedParsingResult, parsingResult.get(0));
		logger.info("Removing of test data.");
		
		try {
			resultChainProductTypes.stream().forEach(testEntityManager::remove);
			testEntityManager.flush();
			logger.info("Test data was cleaned successfully: instances of '{}' were removed from the database.", ChainProductType.class);
		} catch (Exception exception) {
			logger.error("Exception has occurred during the removing of test data ('{}' instances): {}.", ChainProductType.class, exception);
		}
	}
	
	@Test
	public void test_create_basic_chains() {
		MultisheetParsingResult<Chain> expectedParsingResult = new MultisheetParsingResult<>(Chain.class, FILE_WITH_BASIC_DATA);
		expectedParsingResult.setSheetName(SHEET_WITH_BASIC_CHAINS);
		expectedParsingResult.setSheetIndex(0);
		expectedParsingResult.setNumberOfSavedInstances(8);
		expectedParsingResult.setTotalNumberOfInstances(8);
		expectedParsingResult.setStatus(Status.OK);
		List<Chain> expectedChains = new ArrayList<>();
		expectedChains.add(new Chain("БелМаркет", "bel-market", "www.bel-market.by", true));
		expectedChains.add(new Chain("ProStore", "prostore", "www.prostore.by", true));
		expectedChains.add(new Chain("Рублёвский", "rublevski", "www.rublevski.by", true));
		expectedChains.add(new Chain("Соседи", "sosedi", "www.sosedi.by", true));
		expectedChains.add(new Chain("Виталюр", "vitalur", "www.vitalur.by", true));
		expectedChains.add(new Chain("Евроопт", "evroopt", "www.evroopt.by", true));
		expectedChains.add(new Chain("Корона", "korona", "www.korona.by", true));
		expectedChains.add(new Chain("Алми", "almi", "www.almi.by", true));
		List<ParsingResult<?, ?>> parsingResult = dataParser.parseBasicData(Chain.class);
		TypedQuery<Chain> getAllChains = testEntityManager
				.getEntityManager()
				.createQuery("Select chain from Chain chain", Chain.class);
		List<Chain> resultChains = getAllChains.getResultList();
		assertEquals("Result list of chains should contain 8 elements.", 8, resultChains.size());
		assertTrue("Result list of chains should contain all elements of expected list: ["
				+ "{name:'БелМаркет', synonym:'bel-market', link:'www.bel-market.by', logo:null, isActive:true},"
				+ "{name:'ProStore', synonym:'prostore', link:'www.prostore.by', logo:null, isActive:true},"
				+ "{name:'Рублёвский', synonym:'rublevski', link:'www.rublevski.by', logo:null, isActive:true},"
				+ "{name:'Соседи', synonym:'sosedi', link:'www.sosedi.by', logo:null, isActive:true},"
				+ "{name:'Виталюр', synonym:'vitalur', link:'www.vitalur.by', logo:null, isActive:true},"
				+ "{name:'Евроопт', synonym:'evroopt', link:'www.evroopt.by', logo:null, isActive:true},"
				+ "{name:'Корона', synonym:'korona', link:'www.korona.by', logo:null, isActive:true},"
				+ "{name:'Алми', synonym:'almi', link:'www.almi.by', logo:null, isActive:true}"
				+ "].", resultChains.containsAll(expectedChains));
		assertEquals("Parsing result should be:"
				+ "target class - naakcii.by.api.chain.Chain;"
				+ "file - src/test/resources/Basic_data.xlsx;"
				+ "sheet name - Basic_chains;"
				+ "sheet index - 0;"
				+ "status - OK;"
				+ "total number of instances - 8;"
				+ "number of saved instances - 8;"
				+ "number of unsaved instances - 0,"
				+ "including"
				+ "number of already existing instances - 0;"
				+ "number of invalid instances - 0;"
				+ "number of common warnings - 0;"
				+ "number of common exceptions - 0;"
				+ "warnings - 0;"
				+ "constraint violations - 0;"
				+ "exceptions - 0."
				, expectedParsingResult, parsingResult.get(0));
		logger.info("Removing of test data.");
		
		try {
			resultChains.stream().forEach(testEntityManager::remove);
			testEntityManager.flush();
			logger.info("Test data was cleaned successfully: instances of '{}' were removed from the database.", Chain.class);
		} catch (Exception exception) {
			logger.error("Exception has occurred during the removing of test data ('{}' instances): {}.", Chain.class, exception);
		}	
	}
	
	@Test
	public void test_create_basic_categories() {
		MultisheetParsingResult<Category> expectedParsingResult = new MultisheetParsingResult<>(Category.class, FILE_WITH_BASIC_DATA);
		expectedParsingResult.setSheetName(SHEET_WITH_BASIC_CATEGORIES);
		expectedParsingResult.setSheetIndex(1);
		expectedParsingResult.setNumberOfSavedInstances(12);
		expectedParsingResult.setTotalNumberOfInstances(12);
		expectedParsingResult.setStatus(Status.OK);
		List<Category> expectedCategories = new ArrayList<>();
		expectedCategories.add(new Category(INDEFINITE_CATEGORY, false));
		expectedCategories.add(new Category("Молочные продукты, яйца", true));
		expectedCategories.add(new Category("Хлебобулочные изделия", true));
		expectedCategories.add(new Category("Овощи и фрукты", true));
		expectedCategories.add(new Category("Мясо и колбасные изделия", true));
		expectedCategories.add(new Category("Напитки, кофе, чай, соки", true));
		expectedCategories.add(new Category("Бакалея", true));
		expectedCategories.add(new Category("Рыба и морепродукты", true));
		expectedCategories.add(new Category("Сладости", true));
		expectedCategories.add(new Category("Замороженные продукты", true));
		expectedCategories.add(new Category("Детское питание", true));
		expectedCategories.add(new Category("Собственное производство", true));
		List<ParsingResult<?, ?>> parsingResult = dataParser.parseBasicData(Category.class);
		TypedQuery<Category> getAllCategories = testEntityManager
				.getEntityManager()
				.createQuery("Select category from Category category", Category.class);
		List<Category> resultCategories = getAllCategories.getResultList();
		assertEquals("Result list of categories should contain 12 elements.", 12, resultCategories.size());
		assertTrue("Result list of categories should contain all elements of expected list: ["
				+ "{name:'Indefinite category', isActive:false},"
				+ "{name:'Молочные продукты, яйца', isActive:true},"
				+ "{name:'Хлебобулочные изделия', isActive:true},"
				+ "{name:'Овощи и фрукты', isActive:true},"
				+ "{name:'Мясо и колбасные изделия', isActive:true},"
				+ "{name:'Напитки, кофе, чай, соки', isActive:true},"
				+ "{name:'Бакалея', isActive:true},"
				+ "{name:'Рыба и морепродукты', isActive:true},"
				+ "{name:'Сладостиа', isActive:true},"
				+ "{name:'Замороженные продукты', isActive:true},"
				+ "{name:'Детское питание', isActive:true},"
				+ "{name:'Собственное производство', isActive:true}"				
				+ "].", resultCategories.containsAll(expectedCategories));
		assertEquals("Parsing result should be:"
				+ "target class - naakcii.by.api.category.Category;"
				+ "file - src/test/resources/Basic_data.xlsx;"
				+ "sheet name - Basic_categories;"
				+ "sheet index - 1;"
				+ "status - OK;"
				+ "total number of instances - 12;"
				+ "number of saved instances - 12;"
				+ "number of unsaved instances - 0,"
				+ "including"
				+ "number of already existing instances - 0;"
				+ "number of invalid instances - 0;"
				+ "number of common warnings - 0;"
				+ "number of common exceptions - 0;"
				+ "warnings - 0;"
				+ "constraint violations - 0;"
				+ "exceptions - 0."
				, expectedParsingResult, parsingResult.get(0));
		logger.info("Removing of test data.");
		
		try {
			resultCategories.stream().forEach(testEntityManager::remove);
			testEntityManager.flush();
			logger.info("Test data was cleaned successfully: instances of '{}' were removed from the database.", Category.class);
		} catch (Exception exception) {
			logger.error("Exception has occurred during the removing of test data ('{}' instances): {}.", Category.class, exception);
		}	
	}
	
	@Test
	public void test_create_basic_subcategories() {
		logger.info("Preparing of test data.");
		Category firstCategory = new Category(INDEFINITE_CATEGORY, false);
		Category secondCategory = new Category("Молочные продукты, яйца", true);
		Category thirdCategory = new Category("Хлебобулочные изделия", true);
		Category fourthCategory = new Category("Овощи и фрукты", true);
		Category fifthCategory = new Category("Мясо и колбасные изделия", true);
		Category sixthCategory = new Category("Напитки, кофе, чай, соки", true);
		Category seventhCategory = new Category("Бакалея", true);
		Category eighthCategory = new Category("Рыба и морепродукты", true);
		Category ninthCategory = new Category("Сладости", true);
		Category tenthCategory = new Category("Замороженные продукты", true);
		Category eleventhCategory = new Category("Детское питание", true);
		Category twelvethCategory = new Category("Собственное производство", true);
		List<Category> categories = new ArrayList<>();
		
		try {			
			categories.add(testEntityManager.persist(firstCategory));
			categories.add(testEntityManager.persist(secondCategory));
			categories.add(testEntityManager.persist(thirdCategory));
			categories.add(testEntityManager.persist(fourthCategory));
			categories.add(testEntityManager.persist(fifthCategory));
			categories.add(testEntityManager.persist(sixthCategory));
			categories.add(testEntityManager.persist(seventhCategory));
			categories.add(testEntityManager.persist(eighthCategory));
			categories.add(testEntityManager.persist(ninthCategory));
			categories.add(testEntityManager.persist(tenthCategory));
			categories.add(testEntityManager.persist(eleventhCategory));
			categories.add(testEntityManager.persist(twelvethCategory));	
			testEntityManager.clear();
			logger.info("Test data was created successfully: instances of '{}' were added to the database.", Category.class);
		} catch (Exception exception) {
			logger.error("Exception has occurred during the creation of test data ('{}' instances): {}.", Category.class, exception);
		}	
		
		MultisheetParsingResult<Subcategory> expectedParsingResult = new MultisheetParsingResult<>(Subcategory.class, FILE_WITH_BASIC_DATA);
		expectedParsingResult.setSheetName(SHEET_WITH_BASIC_SUBCATEGORIES);
		expectedParsingResult.setSheetIndex(2);
		expectedParsingResult.setNumberOfSavedInstances(59);
		expectedParsingResult.setTotalNumberOfInstances(59);
		expectedParsingResult.setStatus(Status.OK);
		List<Subcategory> expectedSubcategories = new ArrayList<>();
		expectedSubcategories.add(new Subcategory(INDEFINITE_SUBCATEGORY, false, firstCategory));
		expectedSubcategories.add(new Subcategory("Кисломолочные изделия", true, secondCategory));
		expectedSubcategories.add(new Subcategory("Масло", true, secondCategory));
		expectedSubcategories.add(new Subcategory("Молоко", true, secondCategory));
		expectedSubcategories.add(new Subcategory("Мороженое", true, secondCategory));
		expectedSubcategories.add(new Subcategory("Сметана", true, secondCategory));
		expectedSubcategories.add(new Subcategory("Сыр", true, secondCategory));
		expectedSubcategories.add(new Subcategory("Творожные продукты", true, secondCategory));
		expectedSubcategories.add(new Subcategory("Яйцо", true, secondCategory));
		expectedSubcategories.add(new Subcategory("Сдобные изделия", true, thirdCategory));
		expectedSubcategories.add(new Subcategory("Хлеб, батон", true, thirdCategory));
		expectedSubcategories.add(new Subcategory("Хлебцы", true, thirdCategory));
		expectedSubcategories.add(new Subcategory("Грибы", true, fourthCategory));
		expectedSubcategories.add(new Subcategory("Овощи", true, fourthCategory));
		expectedSubcategories.add(new Subcategory("Фрукты", true, fourthCategory));
		expectedSubcategories.add(new Subcategory("Ягоды", true, fourthCategory));
		expectedSubcategories.add(new Subcategory("Говядина", true, fifthCategory));
		expectedSubcategories.add(new Subcategory("Колбасные изделия", true, fifthCategory));
		expectedSubcategories.add(new Subcategory("Копчености", true, fifthCategory));
		expectedSubcategories.add(new Subcategory("Птица", true, fifthCategory));
		expectedSubcategories.add(new Subcategory("Свинина", true, fifthCategory));
		expectedSubcategories.add(new Subcategory("Другие виды мяса", true, fifthCategory));
		expectedSubcategories.add(new Subcategory("Вода", true, sixthCategory));
		expectedSubcategories.add(new Subcategory("Кофе и заменители", true, sixthCategory));
		expectedSubcategories.add(new Subcategory("Напитки", true, sixthCategory));
		expectedSubcategories.add(new Subcategory("Соки", true, sixthCategory));
		expectedSubcategories.add(new Subcategory("Чай", true, sixthCategory));
		expectedSubcategories.add(new Subcategory("Консервированная продукция", true, seventhCategory));
		expectedSubcategories.add(new Subcategory("Крупы, бобовые", true, seventhCategory));
		expectedSubcategories.add(new Subcategory("Макароны", true, seventhCategory));
		expectedSubcategories.add(new Subcategory("Масло растительное, уксус", true, seventhCategory));
		expectedSubcategories.add(new Subcategory("Мука", true, seventhCategory));
		expectedSubcategories.add(new Subcategory("Продукты быстрого приготовления", true, seventhCategory));
		expectedSubcategories.add(new Subcategory("Снеки", true, seventhCategory));
		expectedSubcategories.add(new Subcategory("Соусы", true, seventhCategory));
		expectedSubcategories.add(new Subcategory("Специи и кондитерские добавки", true, seventhCategory));
		expectedSubcategories.add(new Subcategory("Сухие завтраки", true, seventhCategory));
		expectedSubcategories.add(new Subcategory("Сухофрукты", true, seventhCategory));
		expectedSubcategories.add(new Subcategory("Закуски из рыбы и морепродуктов", true, eighthCategory));
		expectedSubcategories.add(new Subcategory("Икра", true, eighthCategory));
		expectedSubcategories.add(new Subcategory("Морепродукты", true, eighthCategory));
		expectedSubcategories.add(new Subcategory("Рыба", true, eighthCategory));
		expectedSubcategories.add(new Subcategory("Бисквиты и печенье", true, ninthCategory));
		expectedSubcategories.add(new Subcategory("Другие сладости", true, ninthCategory));
		expectedSubcategories.add(new Subcategory("Торты, пирожные", true, ninthCategory));
		expectedSubcategories.add(new Subcategory("Шоколад и конфеты", true, ninthCategory));
		expectedSubcategories.add(new Subcategory("Грибы", true, tenthCategory));
		expectedSubcategories.add(new Subcategory("Овощи", true, tenthCategory));
		expectedSubcategories.add(new Subcategory("Полуфабрикаты", true, tenthCategory));
		expectedSubcategories.add(new Subcategory("Ягоды, фрукты", true, tenthCategory));
		expectedSubcategories.add(new Subcategory("Каши", true, eleventhCategory));
		expectedSubcategories.add(new Subcategory("Консервы", true, eleventhCategory));
		expectedSubcategories.add(new Subcategory("Пюре", true, eleventhCategory));
		expectedSubcategories.add(new Subcategory("Соки", true, eleventhCategory));
		expectedSubcategories.add(new Subcategory("Сухие смеси", true, eleventhCategory));
		expectedSubcategories.add(new Subcategory("Вторые блюда", true, twelvethCategory));
		expectedSubcategories.add(new Subcategory("Десерты", true, twelvethCategory));
		expectedSubcategories.add(new Subcategory("Салаты и закуски", true, twelvethCategory));
		expectedSubcategories.add(new Subcategory("Супы", true, twelvethCategory));
		List<ParsingResult<?, ?>> parsingResult = dataParser.parseBasicData(Subcategory.class);
		TypedQuery<Subcategory> getAllSubcategories = testEntityManager
				.getEntityManager()
				.createQuery("Select subcategory from Subcategory subcategory", Subcategory.class);
		List<Subcategory> resultSubcategories = getAllSubcategories.getResultList();
		assertEquals("Result list of subcategories should contain 59 elements.", 59, resultSubcategories.size());
		assertTrue("Result list of subcategories should contain all elements of expected list: ["
				+ "{name:'Indefinite subcategory', isActive:false},"
				+ "{name:'Кисломолочные изделия', isActive:true},"
				+ "{name:'Молоко', isActive:true},"
				+ "{name:'Мороженое', isActive:true},"
				+ "{name:'Сметана', isActive:true},"
				+ "{name:'Сыр', isActive:true},"
				+ "{name:'Творожные продукты', isActive:true},"
				+ "{name:'Яйцо', isActive:true},"
				+ "{name:'Сдобные изделия', isActive:true},"
				+ "{name:'Хлеб, батон', isActive:true},"
				+ "{name:'Хлебцы', isActive:true},"
				+ "{name:'Грибы', isActive:true},"
				+ "{name:'Овощи', isActive:true},"
				+ "{name:'Фрукты', isActive:true},"
				+ "{name:'Ягоды', isActive:true}"
				+ "..."
				+ "{name:'Супы', isActive:true}"
				+ "].", resultSubcategories.containsAll(expectedSubcategories));
		assertEquals("Parsing result should be:"
				+ "target class - naakcii.by.api.subcategory.Subcategory;"
				+ "file - src/test/resources/Basic_data.xlsx;"
				+ "sheet name - Basic_subcategories;"
				+ "sheet index - 2;"
				+ "status - OK;"
				+ "total number of instances - 59;"
				+ "number of saved instances - 59;"
				+ "number of unsaved instances - 0,"
				+ "including"
				+ "number of already existing instances - 0;"
				+ "number of invalid instances - 0;"
				+ "number of common warnings - 0;"
				+ "number of common exceptions - 0;"
				+ "warnings - 0;"
				+ "constraint violations - 0;"
				+ "exceptions - 0."
				, expectedParsingResult, parsingResult.get(0));
		logger.info("Removing of test data.");
		
		try {
			resultSubcategories.stream().map(Subcategory::getCategory).distinct().forEach(testEntityManager::remove);
			testEntityManager.flush();
			logger.info("Test data was cleaned successfully: instances of '{}' and '{}' were removed from the database.", Category.class, Subcategory.class);
		} catch (Exception exception) {
			logger.error("Exception has occurred during the removing of test data ('{}' and '{}' instances): {}.", 
					Category.class, Subcategory.class, exception);
		}
	}
	
	@Test
	public void test_parse_chain_products() {
		logger.info("Preparing of test data.");
		Category indefiniteCategory = new Category(INDEFINITE_CATEGORY, false);
		Subcategory indefiniteSubcategory = new Subcategory(INDEFINITE_SUBCATEGORY, false, indefiniteCategory);
		
		try {
			testEntityManager.persist(indefiniteCategory);
			logger.info("Test data was created successfully: instances of '{}' and '{}' were added to the database.", 
					Category.class, Subcategory.class);
		} catch (Exception exception) {
			logger.error("Exception has occurred during the creation of test data ('{} and '{}' instances): {}.", 
					Category.class, Subcategory.class, exception);
		}
		
		List<Country> countries = Arrays.stream(CountryCode.values()).map(Country::new).collect(Collectors.toList());
		
		try {
			countries.forEach(testEntityManager::persist);
			logger.info("Test data was created successfully: instances of '{}' were added to the database.", Country.class);
		} catch (Exception exception) {
			logger.error("Exception has occurred during the creation of test data ('{} instances): {}.", Country.class, exception);
		}
		
		UnitOfMeasure kilo = new UnitOfMeasure(UnitCode.KG);
		UnitOfMeasure piece = new UnitOfMeasure(UnitCode.PC);
		
		try {
			testEntityManager.persist(kilo);
			testEntityManager.persist(piece);
			logger.info("Test data was created successfully: instances of '{}' were added to the database.", UnitOfMeasure.class);
		} catch (Exception exception) {
			logger.error("Exception has occurred during the creation of test data ('{} instances): {}.", UnitOfMeasure.class, exception);
		}
		
		Chain almiChain = new Chain("Алми", "almi", "www.almi.by", true);
		
		try {
			testEntityManager.persist(almiChain);
			logger.info("Test data was created successfully: instance of '{}' was added to the database.", Chain.class);
		} catch (Exception exception) {
			logger.error("Exception has occurred during the creation of test data ('{} instance): {}.", Chain.class, exception);
		}
		
		ChainProductType discountType = new ChainProductType("Скидка", "discount");
		ChainProductType goodPriceType = new ChainProductType("Хорошая цена", "good_price");
		ChainProductType onePlusOneType = new ChainProductType("1+1", "one_plus_one");
		
		try {
			testEntityManager.persist(discountType);
			testEntityManager.persist(goodPriceType);
			testEntityManager.persist(onePlusOneType);
			testEntityManager.clear();
			logger.info("Test data was created successfully: instances of '{}' were added to the database.", ChainProductType.class);
		} catch (Exception exception) {
			logger.error("Exception has occurred during the creation of test data ('{} instances): {}.", ChainProductType.class, exception);
		}
		
		MultisheetParsingResult<Product> expectedProductParsingResult = new MultisheetParsingResult<>(Product.class, FILE_WITH_CHAIN_PRODUCTS);
		expectedProductParsingResult.setSheetName(SHEET_WITH_CHAIN_PRODUCTS);
		expectedProductParsingResult.setSheetIndex(0);
		expectedProductParsingResult.setNumberOfSavedInstances(10);
		expectedProductParsingResult.setTotalNumberOfInstances(10);
		expectedProductParsingResult.setStatus(Status.OK);
		MultisheetParsingResult<ChainProduct> expectedChainProductParsingResult = new MultisheetParsingResult<>(ChainProduct.class, FILE_WITH_CHAIN_PRODUCTS);
		expectedChainProductParsingResult.setSheetName(SHEET_WITH_CHAIN_PRODUCTS);
		expectedChainProductParsingResult.setSheetIndex(0);
		expectedChainProductParsingResult.setNumberOfSavedInstances(10);
		expectedChainProductParsingResult.setTotalNumberOfInstances(10);
		expectedChainProductParsingResult.setStatus(Status.OK);
		List<ChainProduct> expectedChainProducts = new ArrayList<>();
		expectedChainProducts.add(new ChainProduct(new Product("2100220000000", "Апельсины крупные Импорт Вес", kilo, true, indefiniteSubcategory), 
												   almiChain, new BigDecimal("3.69"), new BigDecimal("3.19"), new BigDecimal("14"), discountType, 
												   new GregorianCalendar(2018, 10, 22), new GregorianCalendar(2019, 10, 28)));
		expectedChainProducts.add(new ChainProduct(new Product("4607037121607", "Сыр плавл President мастер бутерброда ветчина жир 40% 150г", piece, true, indefiniteSubcategory), 
												   almiChain, new BigDecimal("3.25"), new BigDecimal("2.39"), new BigDecimal("26"), discountType, 
												   new GregorianCalendar(2018, 10, 22), new GregorianCalendar(2019, 10, 28)));
		expectedChainProducts.add(new ChainProduct(new Product("4770190038461", "Мясо крабовое Vici охлажденные 200г в/у", piece, true, indefiniteSubcategory), 
												   almiChain, new BigDecimal("2.45"), new BigDecimal("1.65"), new BigDecimal("33"), discountType, 
												   new GregorianCalendar(2018, 10, 22), new GregorianCalendar(2019, 10, 28)));
		expectedChainProducts.add(new ChainProduct(new Product("4740056095082", "Фигурки рыбные Vici Дракоша 240г", piece, true, indefiniteSubcategory), 
												   almiChain, new BigDecimal("2.98"), new BigDecimal("1.99"), new BigDecimal("33"), discountType, 
												   new GregorianCalendar(2018, 10, 22), new GregorianCalendar(2019, 10, 28)));
		expectedChainProducts.add(new ChainProduct(new Product("8722700343486", "Суп чашка-супа Knorr сырный с сухариками 16г", piece, true, indefiniteSubcategory), 
												   almiChain, new BigDecimal("0.78"), new BigDecimal("0.59"), new BigDecimal("24"), discountType, 
												   new GregorianCalendar(2018, 10, 22), new GregorianCalendar(2019, 10, 28)));
		expectedChainProducts.add(new ChainProduct(new Product("4607029348166", "Набор пирожных Муравейник 420г", piece, true, indefiniteSubcategory), 
												   almiChain, new BigDecimal("5.65"), new BigDecimal("4.39"), new BigDecimal("22"), discountType, 
												   new GregorianCalendar(2018, 10, 22), new GregorianCalendar(2019, 10, 28)));
		expectedChainProducts.add(new ChainProduct(new Product("4791045003182", "Чай черный Hyleys английский Earl Grey с бергамотом кр/л 100г", piece, true, indefiniteSubcategory), 
												   almiChain, new BigDecimal("4.99"), new BigDecimal("3.99"), new BigDecimal("20"), discountType, 
												   new GregorianCalendar(2018, 10, 22), new GregorianCalendar(2019, 10, 28)));
		expectedChainProducts.add(new ChainProduct(new Product("2102336000000", "Хурма Каки Вес", kilo, true, indefiniteSubcategory), 
												   almiChain, new BigDecimal("4.49"), new BigDecimal("2.89"), new BigDecimal("36"), discountType, 
												   new GregorianCalendar(2018, 10, 22), new GregorianCalendar(2019, 10, 28)));
		expectedChainProducts.add(new ChainProduct(new Product("2102354000000", "Киви Импорт Вес", kilo, true, indefiniteSubcategory), 
											 	   almiChain, new BigDecimal("3.49"), new BigDecimal("3.39"), new BigDecimal("3"), discountType, 
											 	   new GregorianCalendar(2018, 10, 22), new GregorianCalendar(2019, 10, 28)));
		expectedChainProducts.add(new ChainProduct(new Product("2103947000000", "Минтай с/м б/г Вес", kilo, true, indefiniteSubcategory), 
												   almiChain, new BigDecimal("4.79"), new BigDecimal("4.09"), new BigDecimal("15"), discountType, 
												   new GregorianCalendar(2018, 10, 22), new GregorianCalendar(2019, 10, 28)));
		List<ParsingResult<?, ?>> parsingResult = dataParser.parseChainProducts(FILE_WITH_CHAIN_PRODUCTS, "almi");
		TypedQuery<Product> getAllProducts = testEntityManager
				.getEntityManager()
				.createQuery("Select product from Product product", Product.class);
		List<Product> resultProducts = getAllProducts.getResultList();
		TypedQuery<ChainProduct> getAllChainProducts = testEntityManager
				.getEntityManager()
				.createQuery("Select chainProduct from ChainProduct chainProduct", ChainProduct.class);
		List<ChainProduct> resultChainProducts = getAllChainProducts.getResultList();
		assertEquals("Result list of chain products should contain 10 elements.", 10, resultChainProducts.size());
		assertEquals("Result list of products should contain 10 elements.", 10, resultProducts.size());
		assertTrue("Result list of chain products should contain all elements of expected list: ["
				+ "{"
					+ "product:{name:'Апельсины крупные Импорт Вес', barcode:'2100220000000', unit:'кг', isActive:true},"
					+ "chain:{name:'Алми', synonym:'almi', link:'www.almi.by', logo:null, isActive:true},"
					+ "type:{name:'Скидка', synonym:'discount'},"
					+ "basePrice:3.69, discountPrice:3.19, discountPercent:14, startDate:'22.11.2018',  endDate:'22.11.2019'"
				+ "},"
				+ "{"
					+ "product:{name:'Сыр плавл President мастер бутерброда ветчина жир 40% 150г', barcode:'4607037121607', unit:'шт', isActive:true},"
					+ "chain:{name:'Алми', synonym:'almi', link:'www.almi.by', logo:null, isActive:true},"
					+ "type:{name:'Скидка', synonym:'discount'},"
					+ "basePrice:3.25, discountPrice:2.39, discountPercent:26, startDate:'22.11.2018',  endDate:'22.11.2019'"
				+ "},"
				+ "{"
					+ "product:{name:'Мясо крабовое Vici охлажденные 200г в/у', barcode:'4770190038461', unit:'шт', isActive:true},"
					+ "chain:{name:'Алми', synonym:'almi', link:'www.almi.by', logo:null, isActive:true},"
					+ "type:{name:'Скидка', synonym:'discount'},"
					+ "basePrice:2.45, discountPrice:1.65, discountPercent:33, startDate:'22.11.2018',  endDate:'22.11.2019'"
				+ "},"	
				+ "{"
					+ "product:{name:'Фигурки рыбные Vici Дракоша 240г', barcode:'4740056095082', unit:'шт', isActive:true},"
					+ "chain:{name:'Алми', synonym:'almi', link:'www.almi.by', logo:null, isActive:true},"
					+ "type:{name:'Скидка', synonym:'discount'},"
					+ "basePrice:2.98, discountPrice:1.69, discountPercent:33, startDate:'22.11.2018',  endDate:'22.11.2019'"
				+ "},"
				+ "{"
					+ "product:{name:'Суп чашка-супа Knorr сырный с сухариками 16г', barcode:'8722700343486', unit:'шт', isActive:true},"
					+ "chain:{name:'Алми', synonym:'almi', link:'www.almi.by', logo:null, isActive:true},"
					+ "type:{name:'Скидка', synonym:'discount'},"
					+ "basePrice:0.78, discountPrice:0.59, discountPercent:24, startDate:'22.11.2018',  endDate:'22.11.2019'"
				+ "},"
				+ "{"
					+ "product:{name:'Набор пирожных Муравейник 420г', barcode:'4607029348166', unit:'шт', isActive:true},"
					+ "chain:{name:'Алми', synonym:'almi', link:'www.almi.by', logo:null, isActive:true},"
					+ "type:{name:'Скидка', synonym:'discount'},"
					+ "basePrice:5.65, discountPrice:4.39, discountPercent:22, startDate:'22.11.2018',  endDate:'22.11.2019'"
				+ "},"
				+ "{"
					+ "product:{name:'Чай черный Hyleys английский Earl Grey с бергамотом кр/л 100г', barcode:'4791045003182', unit:'шт', isActive:true},"
					+ "chain:{name:'Алми', synonym:'almi', link:'www.almi.by', logo:null, isActive:true},"
					+ "type:{name:'Скидка', synonym:'discount'},"
					+ "basePrice:4.99, discountPrice:3.99, discountPercent:20, startDate:'22.11.2018',  endDate:'22.11.2019'"
				+ "},"
				+ "{"
					+ "product:{name:'Хурма Каки Вес', barcode:'2102336000000', unit:'кг', isActive:true},"
					+ "chain:{name:'Алми', synonym:'almi', link:'www.almi.by', logo:null, isActive:true},"
					+ "type:{name:'Скидка', synonym:'discount'},"
					+ "basePrice:4.49, discountPrice:2.89, discountPercent:36, startDate:'22.11.2018',  endDate:'22.11.2019'"
				+ "},"
				+ "{"
					+ "product:{name:'Киви Импорт Вес', barcode:'2102354000000', unit:'кг', isActive:true},"
					+ "chain:{name:'Алми', synonym:'almi', link:'www.almi.by', logo:null, isActive:true},"
					+ "type:{name:'Скидка', synonym:'discount'},"
					+ "basePrice:3.49, discountPrice:3.39, discountPercent:3, startDate:'22.11.2018',  startDate:'22.11.2019'"
				+ "},"
				+ "{"
					+ "product:{name:'Минтай с/м б/г Вес', barcode:'2103947000000', unit:'кг', isActive:true},"
					+ "chain:{name:'Алми', synonym:'almi', link:'www.almi.by', logo:null, isActive:true},"
					+ "type:{name:'Скидка', synonym:'discount'},"
					+ "basePrice:4.79, discountPrice:4.09, discountPercent:15, startDate:'22.11.2018',  startDate:'22.11.2019'"
				+ "}]", resultChainProducts.containsAll(expectedChainProducts));
		assertEquals("Product parsing result should be:"
				+ "target class - naakcii.by.api.product.Product;"
				+ "file - src/test/resources/Test_chain_products.xlsx;"
				+ "sheet name - Chain_products;"
				+ "sheet index - 0;"
				+ "status - OK;"
				+ "total number of instances - 10;"
				+ "number of saved instances - 10;"
				+ "number of unsaved instances - 0,"
				+ "including"
				+ "number of already existing instances - 0;"
				+ "number of invalid instances - 0;"
				+ "number of common warnings - 0;"
				+ "number of common exceptions - 0;"
				+ "warnings - 0;"
				+ "constraint violations - 0;"
				+ "exceptions - 0."
				, expectedProductParsingResult, parsingResult.get(0));
		assertEquals("Chain product parsing result should be:"
				+ "target class - naakcii.by.api.chainproduct.Chainproduct;"
				+ "file - src/test/resources/Test_chain_products.xlsx;"
				+ "sheet name - Chain_products;"
				+ "sheet index - 0;"
				+ "status - OK;"
				+ "total number of instances - 10;"
				+ "number of saved instances - 10;"
				+ "number of unsaved instances - 0,"
				+ "including"
				+ "number of already existing instances - 0;"
				+ "number of invalid instances - 0;"
				+ "number of common warnings - 0;"
				+ "number of common exceptions - 0;"
				+ "warnings - 0;"
				+ "constraint violations - 0;"
				+ "exceptions - 0."
				, expectedChainProductParsingResult, parsingResult.get(1));
		logger.info("Removing of test data.");
		
		try {
			resultProducts.stream().forEach(testEntityManager::remove);
			resultChainProducts.stream().forEach(testEntityManager::remove);
			testEntityManager.remove(testEntityManager.find(Chain.class, almiChain.getId()));
			testEntityManager.remove(testEntityManager.find(Category.class, indefiniteCategory.getId()));
			testEntityManager.remove(testEntityManager.merge(discountType));
			testEntityManager.remove(testEntityManager.merge(onePlusOneType));
			testEntityManager.remove(testEntityManager.merge(goodPriceType));
			testEntityManager.remove(testEntityManager.merge(kilo));
			testEntityManager.remove(testEntityManager.merge(piece));
			countries.stream().forEach((Country country) -> testEntityManager.remove(testEntityManager.merge(country)));
			testEntityManager.flush();
			logger.info("Test data was cleaned successfully: instances of '{}', '{}', '{}', '{}', '{}', '{}', '{}' and '{}' were removed from the database.", 
					Category.class, Subcategory.class, Product.class, ChainProduct.class, ChainProductType.class, UnitOfMeasure.class, Country.class, Chain.class);
		} catch (Exception exception) {
			logger.error("Exception has occurred during the removing of test data ('{}', '{}', '{}', '{}', '{}', '{}', '{}' and '{}' instances): {}.", 
					Category.class, Subcategory.class, Product.class, ChainProduct.class, ChainProductType.class, UnitOfMeasure.class, Country.class, Chain.class, exception);
			System.out.println(exception.getMessage());
			exception.printStackTrace();
		}
	}
		
	@After
	public void tearDown() {
		dataParser = null;
	}
}
