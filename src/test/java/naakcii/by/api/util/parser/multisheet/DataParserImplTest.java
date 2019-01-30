package naakcii.by.api.util.parser.multisheet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import naakcii.by.api.APIApplication;
import naakcii.by.api.category.Category;
import naakcii.by.api.category.CategoryRepository;
import naakcii.by.api.chain.Chain;
import naakcii.by.api.chain.ChainRepository;
import naakcii.by.api.chainproduct.ChainProductRepository;
import naakcii.by.api.chainproducttype.ChainProductType;
import naakcii.by.api.chainproducttype.ChainProductTypeRepository;
import naakcii.by.api.country.Country;
import naakcii.by.api.country.CountryCode;
import naakcii.by.api.country.CountryRepository;
import naakcii.by.api.product.ProductRepository;
import naakcii.by.api.subcategory.Subcategory;
import naakcii.by.api.subcategory.SubcategoryRepository;
import naakcii.by.api.unitofmeasure.UnitCode;
import naakcii.by.api.unitofmeasure.UnitOfMeasure;
import naakcii.by.api.unitofmeasure.UnitOfMeasureRepository;
import naakcii.by.api.util.ObjectFactory;
import naakcii.by.api.util.parser.DataParser;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = APIApplication.class)
@AutoConfigureTestEntityManager
@Transactional
@TestPropertySource(locations = "classpath:application-integration-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class DataParserImplTest {
	
	private static final Logger logger = LogManager.getLogger(DataParserImplTest.class);
	private static final String FILE_WITH_BASIC_DATA = "src" + File.separator + 
													   "test" + File.separator + 
													   "resources" + File.separator + 
													   "Basic_data.xlsx";
	private static final String FILE_WITH_CHAIN_PRODUCTS = "src" + File.separator + 
														   "test" + File.separator +
														   "resources" + File.separator + 
														   "Test_chain_products.xlsx";
	private static final String SHEET_WITH_BASIC_CHAIN_PRODUCT_TYPES = "Basic_chain_product_types";
	private static final String SHEET_WITH_BASIC_CATEGORIES = "Basic_categories";
	private static final String SHEET_WITH_BASIC_CHAINS = "Basic_chains";
	private static final String SHEET_WITH_BASIC_SUBCATEGORIES = "Basic_subcategories";
	private static final String SHEET_WITH_CHAIN_PRODUCTS = "Chain_products";
	private static final String INDEFINITE_CATEGORY = "Indefinite category";
	private static final String INDEFINITE_SUBCATEGORY = "Indefinite subcategory";
	private static final String[] ACTION_TYPES = {"Скидка", "Хорошая цена", "1+1"};
	
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
		dataParser = new DataParserImpl(
									objectFactory,
									chainRepository,
									categoryRepository,
									subcategoryRepository,
									productRepository,
									actionRepository,
									actionTypeRepository,
									countryRepository,
									unitOfMeasureRepository
								   ); 
	}
	
	@Test
	public void test_create_countries() {
		
		//dataParser.parseBasicData();
		//dataParser.parseChainProducts(FILE_WITH_CHAIN_PRODUCTS, "almi");
		//dataParser.createBasicActionTypes();
		/*System.out.println(Country.class.getName());
		System.out.println(Country.class);
		List<Class> l;
		if (Country.class.equals(Country.class)) {
			System.out.println("TRUE");
		}
		Class<?>[] classes = {naakcii.by.api.country.Country.class, naakcii.by.api.chain.Chain.class};
		
		List<Class<?>> cl = Arrays.asList(classes);
		
		if (cl.contains(Country.class)) {
			System.out.println("Country");
		}
		
		if (cl.contains(naakcii.by.api.category.Category.class)) {
			System.out.println("Category");
		}
		/*ParsingResult<Country> expectedParsingResult = new ParsingResult<>(Country.class, CountryCode.class.getName());
		expectedParsingResult.setNumberOfSavedInstances(101);
		expectedParsingResult.setTotalNumberOfInstances(101);
		ParsingResult<Country> parsingResult = dataParser.createCountries();
		List<Country> expectedCountries = Arrays.stream(CountryCode.values()).map(Country::new).collect(Collectors.toList());
		TypedQuery<Country> getAllCountries = testEntityManager
				.getEntityManager()
				.createQuery("Select country from Country country", Country.class);
		List<Country> resultCountries = getAllCountries.getResultList();
		assertEquals("Result list of countries should contain 101 elements.", 101, resultCountries.size());
		assertTrue("Result list of countries should contain all elements of expected list: ["
				+ "{name:'Австралия', alphaCode2:'AU', alphaCode3:'AUS'},"
				+ "..."
				+ "{name:'Япония', alphaCode2:'JP', alphaCode3:'JPN'}"
				+ "].", resultCountries.containsAll(expectedCountries));
		assertEquals("Parsing result should be:"
				+ "target class - naakcii.by.api.country.Country;"
				+ "file - naakcii.by.api.country.CountryCode;"
				+ "sheet - null;"
				+ "sheet index - null;"
				+ "total number of instances - 101;"
				+ "number of saved instances - 101;"
				+ "number of unsaved instances - 0,"
				+ "including"
				+ "number of already existing instances - 0;"
				+ "number of invalid instances - 0;"
				+ "common warnings: not found;"
				+ "common exceptions: not found;"
				+ "warnings: not found;"
				+ "constraint violations: not found;"
				+ "exceptions: not found."
				, expectedParsingResult, parsingResult);
		logger.info("Removing of test data.");
		
		try {
			resultCountries.stream().forEach(testEntityManager::remove);
			testEntityManager.flush();
			logger.info("Test data was cleaned successfully: instances of '{}' were removed from the database.", Country.class);
		} catch (Exception exception) {
			logger.error("Exception has occurred during the removing of test data ('{}' instances): {}.", 
					Country.class, exception);
		}*/
	}
/*	
	@Test
	public void test_create_action_types() {
		ParsingResult<ChainProductType> expectedParsingResult = new ParsingResult<>(ChainProductType.class, FILE_WITH_BASIC_DATA);
		expectedParsingResult.setSheetName(SHEET_WITH_BASIC_ACTION_TYPES);
		expectedParsingResult.setSheetIndex(3);
		expectedParsingResult.setNumberOfSavedInstances(3);
		expectedParsingResult.setTotalNumberOfInstances(3);
		List<ChainProductType> expectedActionTypes = Arrays.stream(ACTION_TYPES).map(ChainProductType::new).collect(Collectors.toList());
		ParsingResult<ChainProductType> parsingResult = dataParser.createBasicActionTypes();
		TypedQuery<ChainProductType> getAllActionTypes = testEntityManager
				.getEntityManager()
				.createQuery("Select actionType from ActionType actionType", ChainProductType.class);
		List<ChainProductType> resultActionTypes = getAllActionTypes.getResultList();
		assertEquals("Result list of action types should contain 3 elements.", 3, resultActionTypes.size());
		assertTrue("Result list of action types should contain all elements of expected list: ["
				+ "{name:'Скидка', tooltip:null}, "
				+ "{name:'Хорошая цена', tooltip:null}, "
				+ "{name:'1+1', tooltip:null}"
				+ "].",	resultActionTypes.containsAll(expectedActionTypes));
		assertEquals("Parsing result should be:"
				+ "target class - naakcii.by.api.actiontype.ActionType;"
				+ "file - src/test/resources/Basic_data.xlsx;"
				+ "sheet - Basic_action_types;"
				+ "sheet index - 2;"
				+ "total number of instances - 3;"
				+ "number of saved instances - 3;"
				+ "including"
				+ "number of already existing instances - 0;"
				+ "number of invalid instances - 0;"
				+ "common warnings: not found;"
				+ "common exceptions: not found;"
				+ "warnings: not found;"
				+ "constraint violations: not found;"
				+ "exceptions: not found."
				, expectedParsingResult, parsingResult);
		logger.info("Removing of test data.");
		
		try {
			resultActionTypes.stream().forEach((ChainProductType actionType) -> testEntityManager.remove(actionType));
			testEntityManager.flush();
			logger.info("Test data was cleaned successfully: instances of '{}' were removed from the database.", ChainProductType.class);
		} catch (Exception exception) {
			logger.error("Exception has occurred during the removing of test data ('{}' instances): {}.", 
					ChainProductType.class, exception);
		}
	}
	
	@Test
	public void test_create_chains() {
		ParsingResult<Chain> expectedParsingResult = new ParsingResult<>(Chain.class, FILE_WITH_BASIC_DATA);
		expectedParsingResult.setSheetName(SHEET_WITH_BASIC_CHAINS);
		expectedParsingResult.setSheetIndex(0);
		expectedParsingResult.setNumberOfSavedInstances(8);
		expectedParsingResult.setTotalNumberOfInstances(8);
		List<Chain> expectedChains = new ArrayList<>();
		expectedChains.add(new Chain("БелМаркет", "www.bel-market.by", true));
		expectedChains.add(new Chain("ProStore", "www.prostore.by", true));
		expectedChains.add(new Chain("Рублёвский", "www.rublevski.by", true));
		expectedChains.add(new Chain("Соседи", "www.sosedi.by", true));
		expectedChains.add(new Chain("Виталюр", "www.vitalur.by", true));
		expectedChains.add(new Chain("Евроопт", "www.evroopt.by", true));
		expectedChains.add(new Chain("Корона", "www.korona.by", true));
		expectedChains.add(new Chain("Алми", "www.almi.by", true));
		ParsingResult<Chain> parsingResult = dataParser.createBasicChains();
		TypedQuery<Chain> getAllChains = testEntityManager
				.getEntityManager()
				.createQuery("Select chain from Chain chain", Chain.class);
		List<Chain> resultChains = getAllChains.getResultList();
		assertEquals("Result list of chains should contain 8 elements.", 8, resultChains.size());
		assertTrue("Result list of chains should contain all elements of expected list: ["
				+ "{name:'БелМаркет', link:'www.bel-market.by', logo:null, isActive:true},"
				+ "{name:'ProStore', link:'www.prostore.by', logo:null, isActive:true},"
				+ "{name:'Рублёвский', link:'www.rublevski.by', logo:null, isActive:true},"
				+ "{name:'Соседи', link:'www.sosedi.by', logo:null, isActive:true},"
				+ "{name:'Виталюр', link:'www.vitalur.by', logo:null, isActive:true},"
				+ "{name:'Евроопт', link:'www.evroopt.by', logo:null, isActive:true},"
				+ "{name:'Корона', link:'www.korona.by', logo:null, isActive:true}"
				+ "].", resultChains.containsAll(expectedChains));
		assertEquals("Parsing result should be:"
				+ "target class - naakcii.by.api.chain.Chain;"
				+ "file - src/test/resources/Basic_data.xlsx;"
				+ "sheet - Basic_action_types;"
				+ "sheet index - 0;"
				+ "total number of instances - 7;"
				+ "number of saved instances - 7;"
				+ "including"
				+ "number of already existing instances - 0;"
				+ "number of invalid instances - 0;"
				+ "common warnings: not found;"
				+ "common exceptions: not found;"
				+ "warnings: not found;"
				+ "constraint violations: not found;"
				+ "exceptions: not found."
				, expectedParsingResult, parsingResult);
		logger.info("Removing of test data.");
		
		try {
			resultChains.stream().forEach((Chain chain) -> testEntityManager.remove(chain));
			testEntityManager.flush();
			logger.info("Test data was cleaned successfully: instances of '{}' were removed from the database.", Chain.class);
		} catch (Exception exception) {
			logger.error("Exception has occurred during the removing of test data ('{}' instances): {}.", 
					Chain.class, exception);
		}	
	}
	
	@Test
	public void test_create_categories() {
		ParsingResult<Category> expectedParsingResult = new ParsingResult<>(Category.class, FILE_WITH_BASIC_DATA);
		expectedParsingResult.setSheetName(SHEET_WITH_BASIC_CATEGORIES);
		expectedParsingResult.setSheetIndex(1);
		expectedParsingResult.setNumberOfSavedInstances(12);
		expectedParsingResult.setTotalNumberOfInstances(12);
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
		ParsingResult<Category> parsingResult = dataParser.createBasicCategories();
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
				+ "sheet - Basic_categories;"
				+ "sheet index - 1;"
				+ "total number of instances - 12;"
				+ "number of saved instances - 12;"
				+ "including"
				+ "number of already existing instances - 0;"
				+ "number of invalid instances - 0;"
				+ "common warnings: not found;"
				+ "common exceptions: not found;"
				+ "warnings: not found;"
				+ "constraint violations: not found;"
				+ "exceptions: not found."
				, expectedParsingResult, parsingResult);
		logger.info("Removing of test data.");
		
		try {
			resultCategories.stream().forEach((Category category) -> testEntityManager.remove(category));
			testEntityManager.flush();
			logger.info("Test data was cleaned successfully: instances of '{}' were removed from the database.", Category.class);
		} catch (Exception exception) {
			logger.error("Exception has occurred during the removing of test data ('{}' instances): {}.", 
					Category.class, exception);
		}	
	}
	
	@Test
	public void test_create_subcategories() {
		logger.info("Preparing of test data.");
		Category firstCategory = new Category(INDEFINITE_CATEGORY, false);
		Category secondCategory = new Category("Молочные продукты, яйца", true);
		Category thirdCategory = new Category("Хлебобулочные изделия", true);
		Category fourthCategory = new Category("Овощи и фрукты", true);
		List<Category> categories = new ArrayList<>();
		
		try {			
			categories.add(testEntityManager.persist(firstCategory));
			categories.add(testEntityManager.persist(secondCategory));
			categories.add(testEntityManager.persist(thirdCategory));
			categories.add(testEntityManager.persist(fourthCategory));
						
			testEntityManager.clear();
			logger.info("Test data was created successfully: instances of '{}' and '{}' were added to the database.", Category.class, Subcategory.class);
		} catch (Exception exception) {
			logger.error("Exception has occurred during the creation of test data ('{}' and '{}' instances): {}.", 
					Category.class, Subcategory.class, exception);
		}	
		
		List<Subcategory> expectedSubcategories = new ArrayList<>();
		expectedSubcategories.add(new Subcategory(INDEFINITE_SUBCATEGORY, firstCategory, false));
		expectedSubcategories.add(new Subcategory("Кисломолочные изделия", secondCategory, true));
		expectedSubcategories.add(new Subcategory("Масло", secondCategory, true));
		expectedSubcategories.add(new Subcategory("Молоко", secondCategory, true));
		expectedSubcategories.add(new Subcategory("Мороженое", secondCategory, true));
		expectedSubcategories.add(new Subcategory("Сметана", secondCategory, true));
		expectedSubcategories.add(new Subcategory("Сыр", secondCategory, true));
		expectedSubcategories.add(new Subcategory("Творожные продукты", secondCategory, true));
		expectedSubcategories.add(new Subcategory("Яйцо", secondCategory, true));
		expectedSubcategories.add(new Subcategory("Сдобные изделия", thirdCategory, true));
		expectedSubcategories.add(new Subcategory("Хлеб, батон", thirdCategory, true));
		expectedSubcategories.add(new Subcategory("Хлебцы", thirdCategory, true));
		expectedSubcategories.add(new Subcategory("Грибы", fourthCategory, true));
		expectedSubcategories.add(new Subcategory("Овощи", fourthCategory, true));
		expectedSubcategories.add(new Subcategory("Фрукты", fourthCategory, true));
		expectedSubcategories.add(new Subcategory("Ягоды", fourthCategory, true));
		
		ParsingResult<Subcategory> expectedParsingResult = new ParsingResult<>(Subcategory.class, FILE_WITH_BASIC_DATA);
		expectedParsingResult.setSheetName(SHEET_WITH_BASIC_SUBCATEGORIES);
		expectedParsingResult.setSheetIndex(2);
		expectedParsingResult.setNumberOfSavedInstances(16);
		expectedParsingResult.setTotalNumberOfInstances(16);
		ParsingResult<Subcategory> parsingResult = dataParser.createBasicSubcategories();
		TypedQuery<Subcategory> getAllSubcategories = testEntityManager
				.getEntityManager()
				.createQuery("Select subcategory from Subcategory subcategory", Subcategory.class);
		List<Subcategory> resultSubcategories = getAllSubcategories.getResultList();
		assertEquals("Result list of subcategories should contain 16 elements.", 16, resultSubcategories.size());
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
				+ "].", resultSubcategories.containsAll(expectedSubcategories));
		assertEquals("Parsing result should be:"
				+ "target class - naakcii.by.api.subcategory.Subcategory;"
				+ "file - src/test/resources/Basic_data.xlsx;"
				+ "sheet - Basic_subcategories;"
				+ "sheet index - 2;"
				+ "total number of instances - 16;"
				+ "number of saved instances - 16;"
				+ "including"
				+ "number of already existing instances - 0;"
				+ "number of invalid instances - 0;"
				+ "common warnings: not found;"
				+ "common exceptions: not found;"
				+ "warnings: not found;"
				+ "constraint violations: not found;"
				+ "exceptions: not found."
				, expectedParsingResult, parsingResult);
		logger.info("Removing of test data.");
		
		try {
			resultSubcategories.stream().forEach((Subcategory subcategory) -> testEntityManager.remove(subcategory));
			categories.stream().forEach((Category category) -> testEntityManager.remove(testEntityManager.merge(category)));
			testEntityManager.flush();
			logger.info("Test data was cleaned successfully: instances of '{}' and '{}' were removed from the database.", Category.class, Subcategory.class);
		} catch (Exception exception) {
			logger.error("Exception has occurred during the removing of test data ('{}' and '{}' instances): {}.", 
					Category.class, Subcategory.class, exception);
		}	
	}
	
	@Test
	public void test_create_products() {
		logger.info("Preparing of test data.");
		Category indefiniteCategory = new Category(INDEFINITE_CATEGORY, false);
		Subcategory indefiniteSubcategory = new Subcategory(INDEFINITE_SUBCATEGORY, indefiniteCategory, false);
		indefiniteCategory.getSubcategories().add(indefiniteSubcategory);
		List<Country> countries = Arrays.stream(CountryCode.values()).map(Country::new).collect(Collectors.toList());
				
		try {
			testEntityManager.persist(indefiniteCategory);
			countries.stream().forEach((Country country) -> testEntityManager.persist(country));
			testEntityManager.clear();
			logger.info("Test data was created successfully: instances of '{}', '{}' and '{}' were added to the database.", 
					Category.class, Subcategory.class, Country.class);
		} catch (Exception exception) {
			logger.error("Exception has occurred during the creation of test data ('{}', '{}' and '{}' instances): {}.", 
					Category.class, Subcategory.class, Country.class, exception);
		}
		
		List<Product> expectedProducts = new ArrayList<>();
		expectedProducts.add(new Product("2100220000000", "Апельсины крупные Импорт Вес", Unit.KG, true, indefiniteSubcategory));
		expectedProducts.add(new Product("4607037121607", "Сыр плавл President мастер бутерброда ветчина жир 40% 150г", Unit.PC, true, indefiniteSubcategory));
		expectedProducts.add(new Product("4770190038461", "Мясо крабовое Vici охлажденные 200г в/у", Unit.PC, true, indefiniteSubcategory));
		expectedProducts.add(new Product("4740056095082", "Фигурки рыбные Vici Дракоша 240г", Unit.PC, true, indefiniteSubcategory));
		expectedProducts.add(new Product("8722700343486", "Суп чашка-супа Knorr сырный с сухариками 16г", Unit.PC, true, indefiniteSubcategory));
		expectedProducts.add(new Product("4607029348166", "Набор пирожных Муравейник 420г", Unit.PC, true, indefiniteSubcategory));
		expectedProducts.add(new Product("4791045003182", "Чай черный Hyleys английский Earl Grey с бергамотом кр/л 100г", Unit.PC, true, indefiniteSubcategory));
		expectedProducts.add(new Product("2102336000000", "Хурма Каки Вес", Unit.KG, true, indefiniteSubcategory));
		expectedProducts.add(new Product("2102354000000", "Киви Импорт Вес", Unit.KG, true, indefiniteSubcategory));
		expectedProducts.add(new Product("2103947000000", "Минтай с/м б/г Вес", Unit.KG, true, indefiniteSubcategory));
		ParsingResult<Product> expectedParsingResult = new ParsingResult<>(Product.class, FILE_WITH_ACTIONS);
		expectedParsingResult.setSheetName(SHEET_WITH_ACTIONS);
		expectedParsingResult.setSheetIndex(0);
		expectedParsingResult.setNumberOfSavedInstances(10);
		expectedParsingResult.setTotalNumberOfInstances(10);
		ParsingResult<Product> parsingResult = dataParser.parseProducts(FILE_WITH_ACTIONS);
		TypedQuery<Product> getAllProducts = testEntityManager
				.getEntityManager()
				.createQuery("Select product from Product product", Product.class);
		List<Product> resultProducts = getAllProducts.getResultList();
		assertEquals("Result list of products should contain 10 elements.", 10, resultProducts.size());
		assertTrue("Result list of products should contain all elements of expected list: ["
				+ "{name:'Апельсины крупные Импорт Вес', barcode:'2100220000000', unit:'кг', isActive:true},"
				+ "{name:'Сыр плавл President мастер бутерброда ветчина жир 40% 150г', barcode:'4607037121607', unit:'шт', isActive:true},"
				+ "{name:'Мясо крабовое Vici охлажденные 200г в/у', barcode:'4770190038461', unit:'шт', isActive:true},"
				+ "{name:'Фигурки рыбные Vici Дракоша 240г', barcode:'4740056095082', unit:'шт', isActive:true},"
				+ "{name:'Суп чашка-супа Knorr сырный с сухариками 16г', barcode:'8722700343486', unit:'шт', isActive:true},"
				+ "{name:'Набор пирожных Муравейник 420г', barcode:'4607029348166', unit:'шт', isActive:true},"
				+ "{name:'Чай черный Hyleys английский Earl Grey с бергамотом кр/л 100г', barcode:'4791045003182', unit:'шт', isActive:true},"
				+ "{name:'Хурма Каки Вес', barcode:'2102336000000', unit:'кг', isActive:true},"
				+ "{name:'Киви Импорт Вес', barcode:'2102354000000', unit:'кг', isActive:true},"
				+ "{name:'Минтай с/м б/г Вес', barcode:'2103947000000', unit:'кг', isActive:true},"
				+ "].", resultProducts.containsAll(expectedProducts));
		assertEquals("Parsing result should be:"
				+ "target class - naakcii.by.api.product.Product;"
				+ "file - src/test/resources/Test_actions.xlsx;"
				+ "sheet - Actions;"
				+ "sheet index - 0;"
				+ "total number of instances - 10;"
				+ "number of saved instances - 10;"
				+ "including"
				+ "number of already existing instances - 0;"
				+ "number of invalid instances - 0;"
				+ "common warnings: not found;"
				+ "common exceptions: not found;"
				+ "warnings: not found;"
				+ "constraint violations: not found;"
				+ "exceptions: not found."
				, expectedParsingResult, parsingResult);
		logger.info("Removing of test data.");
		
		try {
			resultProducts.stream().forEach((Product product) -> testEntityManager.remove(product));
			testEntityManager.remove(testEntityManager.merge(indefiniteCategory));
			countries.stream().forEach((Country country) -> testEntityManager.remove(testEntityManager.merge(country)));
			testEntityManager.flush();
			logger.info("Test data was cleaned successfully: instances of '{}', '{}', '{}' and '{}' were removed from the database.", 
					Category.class, Subcategory.class, Country.class, Product.class);
		} catch (Exception exception) {
			logger.error("Exception has occurred during the removing of test data ('{}', '{}', '{}' and '{}' instances): {}.", 
					Category.class, Subcategory.class, Country.class, Product.class, exception);
		}
	}
	
	@Test
	public void test_create_actions() {
		logger.info("Preparing of test data.");
		Category indefiniteCategory = new Category(INDEFINITE_CATEGORY, false);
		Subcategory indefiniteSubcategory = new Subcategory(INDEFINITE_SUBCATEGORY, indefiniteCategory, false);
		indefiniteCategory.getSubcategories().add(indefiniteSubcategory);
		List<Country> countries = Arrays.stream(CountryCode.values()).map(Country::new).collect(Collectors.toList());
		List<ChainProductType> actionTypes = Arrays.stream(ACTION_TYPES).map(ChainProductType::new).collect(Collectors.toList());
		ChainProductType firstType = actionTypes.get(0);
		List<Product> products = new ArrayList<>();
		Product firstProduct = new Product("2100220000000", "Апельсины крупные Импорт Вес", Unit.KG, true, indefiniteSubcategory); 
		Product secondProduct = new Product("4607037121607", "Сыр плавл President мастер бутерброда ветчина жир 40% 150г", Unit.PC, true, indefiniteSubcategory);		
		Product thirdProduct = new Product("4770190038461", "Мясо крабовое Vici охлажденные 200г в/у", Unit.PC, true, indefiniteSubcategory);
		Product fourthProduct = new Product("4740056095082", "Фигурки рыбные Vici Дракоша 240г", Unit.PC, true, indefiniteSubcategory);
		Product fifthProduct = new Product("8722700343486", "Суп чашка-супа Knorr сырный с сухариками 16г", Unit.PC, true, indefiniteSubcategory);
		Product sixthProduct = new Product("4607029348166", "Набор пирожных Муравейник 420г", Unit.PC, true, indefiniteSubcategory);
		Product seventhProduct = new Product("4791045003182", "Чай черный Hyleys английский Earl Grey с бергамотом кр/л 100г", Unit.PC, true, indefiniteSubcategory);
		Product eighthProduct = new Product("2102336000000", "Хурма Каки Вес", Unit.KG, true, indefiniteSubcategory);
		Product ninthProduct = new Product("2102354000000", "Киви Импорт Вес", Unit.KG, true, indefiniteSubcategory);
		Product tenthProduct = new Product("2103947000000", "Минтай с/м б/г Вес", Unit.KG, true, indefiniteSubcategory);
		products.add(firstProduct);
		products.add(secondProduct);
		products.add(thirdProduct);
		products.add(fourthProduct);
		products.add(fifthProduct);
		products.add(sixthProduct);
		products.add(seventhProduct);
		products.add(eighthProduct);
		products.add(ninthProduct);
		products.add(tenthProduct);
		List<Chain> chains = new ArrayList<>();
		Chain firstChain = new Chain("БелМаркет", "www.bel-market.by", true, "bel-market");
		Chain secondChain = new Chain("ProStore", "www.prostore.by", true, "prostore");
		Chain thirdChain = new Chain("Рублёвский", "www.rublevski.by", true, "rublevski");
		Chain fourthChain = new Chain("Соседи", "www.sosedi.by", true, "sosedi");
		Chain fifthChain = new Chain("Евроопт", "www.evroopt.by", true, "evroopt");
		Chain sixthChain = new Chain("Корона", "www.korona.by", true, "korona");
		Chain seventhChain = new Chain("Алми", "www.almi.by", true, "almi");
		chains.add(firstChain);
		chains.add(secondChain);
		chains.add(thirdChain);
		chains.add(fourthChain);
		chains.add(fifthChain);
		chains.add(sixthChain);
		chains.add(seventhChain);
				
		try {
			testEntityManager.persist(indefiniteCategory);
			countries.stream().forEach((Country country) -> testEntityManager.persist(country));
			actionTypes.stream().forEach((ChainProductType actionType) -> testEntityManager.persist(actionType));
			chains.stream().forEach((Chain chain) -> testEntityManager.persist(chain));
			testEntityManager.clear();
			logger.info("Test data was created successfully: instances of '{}', '{}', '{}', '{}', '{}' and '{}' were added to the database.", 
					Category.class, Subcategory.class, Product.class, Country.class, ChainProductType.class, Chain.class);
		} catch (Exception exception) {
			logger.error("Exception has occurred during the creation of test data ('{}', '{}', '{}', '{}', '{}' and '{}' instances): {}.", 
					Category.class, Subcategory.class, Product.class, Country.class, ChainProductType.class, Chain.class);
		}
		
		List<ChainProduct> expectedActions = new ArrayList<>();
		expectedActions.add(new ChainProduct(firstProduct, seventhChain, new BigDecimal("3.69"), new BigDecimal("3.19"), new BigDecimal("14"), firstType, new GregorianCalendar(2018, 10, 22), new GregorianCalendar(2019, 10, 28)));
		expectedActions.add(new ChainProduct(secondProduct, seventhChain, new BigDecimal("3.25"), new BigDecimal("2.39"), new BigDecimal("26"), firstType, new GregorianCalendar(2018, 10, 22), new GregorianCalendar(2019, 10, 28)));
		expectedActions.add(new ChainProduct(thirdProduct, seventhChain, new BigDecimal("2.45"), new BigDecimal("1.65"), new BigDecimal("33"), firstType, new GregorianCalendar(2018, 10, 22), new GregorianCalendar(2019, 10, 28)));
		expectedActions.add(new ChainProduct(fourthProduct, seventhChain, new BigDecimal("2.98"), new BigDecimal("1.99"), new BigDecimal("33"), firstType, new GregorianCalendar(2018, 10, 22), new GregorianCalendar(2019, 10, 28)));
		expectedActions.add(new ChainProduct(fifthProduct, seventhChain, new BigDecimal("0.78"), new BigDecimal("0.59"), new BigDecimal("24"), firstType, new GregorianCalendar(2018, 10, 22), new GregorianCalendar(2019, 10, 28)));
		expectedActions.add(new ChainProduct(sixthProduct, seventhChain, new BigDecimal("5.65"), new BigDecimal("4.39"), new BigDecimal("22"), firstType, new GregorianCalendar(2018, 10, 22), new GregorianCalendar(2019, 10, 28)));
		expectedActions.add(new ChainProduct(seventhProduct, seventhChain, new BigDecimal("4.99"), new BigDecimal("3.99"), new BigDecimal("20"), firstType, new GregorianCalendar(2018, 10, 22), new GregorianCalendar(2019, 10, 28)));
		expectedActions.add(new ChainProduct(eighthProduct, seventhChain, new BigDecimal("4.49"), new BigDecimal("2.89"), new BigDecimal("36"), firstType, new GregorianCalendar(2018, 10, 22), new GregorianCalendar(2019, 10, 28)));
		expectedActions.add(new ChainProduct(ninthProduct, seventhChain, new BigDecimal("3.49"), new BigDecimal("3.39"), new BigDecimal("3"), firstType, new GregorianCalendar(2018, 10, 22), new GregorianCalendar(2019, 10, 28)));
		expectedActions.add(new ChainProduct(tenthProduct, seventhChain, new BigDecimal("4.79"), new BigDecimal("4.09"), new BigDecimal("15"), firstType, new GregorianCalendar(2018, 10, 22), new GregorianCalendar(2019, 10, 28)));
		ParsingResult<ChainProduct> expectedParsingResult = new ParsingResult<>(ChainProduct.class, FILE_WITH_ACTIONS);
		expectedParsingResult.setSheetName(SHEET_WITH_ACTIONS);
		expectedParsingResult.setSheetIndex(0);
		expectedParsingResult.setNumberOfSavedInstances(10);
		expectedParsingResult.setTotalNumberOfInstances(10);
		ParsingResult<ChainProduct> parsingResult = dataParser.parseActions(FILE_WITH_ACTIONS, "almi");
		TypedQuery<ChainProduct> getAllActions = testEntityManager
				.getEntityManager()
				.createQuery("Select action from Action action", ChainProduct.class);
		List<ChainProduct> resultActions = getAllActions.getResultList();
		assertEquals("Result list of products should contain 10 elements.", 10, resultActions.size());
		assertTrue("Result list of actions should contain all elements of expected list: ["
				+ "{basePrice:3.69, discountPrice:3.19, discountPercent:14, startDate:'22.11.2018',  startDate:'22.11.2019'},"
				+ "{basePrice:3.25, discountPrice:2.39, discountPercent:26, startDate:'22.11.2018',  startDate:'22.11.2019'},"
				+ "{basePrice:2.45, discountPrice:1.65, discountPercent:33, startDate:'22.11.2018',  startDate:'22.11.2019'},"
				+ "{basePrice:2.98, discountPrice:1.69, discountPercent:33, startDate:'22.11.2018',  startDate:'22.11.2019'},"
				+ "{basePrice:0.78, discountPrice:0.59, discountPercent:24, startDate:'22.11.2018',  startDate:'22.11.2019'},"
				+ "{basePrice:5.65, discountPrice:4.39, discountPercent:22, startDate:'22.11.2018',  startDate:'22.11.2019'},"
				+ "{basePrice:4.99, discountPrice:3.99, discountPercent:20, startDate:'22.11.2018',  startDate:'22.11.2019'},"
				+ "{basePrice:4.49, discountPrice:2.89, discountPercent:36, startDate:'22.11.2018',  startDate:'22.11.2019'},"
				+ "{basePrice:3.49, discountPrice:3.39, discountPercent:3, startDate:'22.11.2018',  startDate:'22.11.2019'},"
				+ "{basePrice:4.79, discountPrice:4.09, discountPercent:15, startDate:'22.11.2018',  startDate:'22.11.2019'},"
				+ "].", resultActions.containsAll(expectedActions));
		assertEquals("Parsing result should be:"
				+ "target class - naakcii.by.api.action.Action;"
				+ "file - src/test/resources/Test_actions.xlsx;"
				+ "sheet - Actions;"
				+ "sheet index - 0;"
				+ "total number of instances - 10;"
				+ "number of saved instances - 10;"
				+ "including"
				+ "number of already existing instances - 0;"
				+ "number of invalid instances - 0;"
				+ "common warnings: not found;"
				+ "common exceptions: not found;"
				+ "warnings: not found;"
				+ "constraint violations: not found;"
				+ "exceptions: not found."
				, expectedParsingResult, parsingResult);
		logger.info("Removing of test data.");
		
		try {
			testEntityManager.remove(testEntityManager.merge(indefiniteCategory));
			chains.stream().forEach((Chain chain) -> testEntityManager.remove(testEntityManager.merge(chain)));
			countries.stream().forEach((Country country) -> testEntityManager.remove(testEntityManager.merge(country)));
			actionTypes.stream().forEach((ChainProductType actionType) -> testEntityManager.remove(testEntityManager.merge(actionType)));	
			testEntityManager.flush();
			logger.info("Test data was cleaned successfully: instances of '{}', '{}', '{}', '{}', '{}' and '{}' were removed from the database.", 
					Category.class, Subcategory.class, Product.class, Country.class, ChainProductType.class, Chain.class);
		} catch (Exception exception) {
			logger.error("Exception has occurred during the removing of test data ('{}', '{}', '{}', '{}', '{}' and '{}' instances): {}.", 
					Category.class, Subcategory.class, Product.class, Country.class, ChainProductType.class, Chain.class, exception);
		}
	}*/
		
	@After
	public void tearDown() {
		dataParser = null;
	}
}
