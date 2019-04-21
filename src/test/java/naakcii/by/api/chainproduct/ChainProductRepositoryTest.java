package naakcii.by.api.chainproduct;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import naakcii.by.api.unitofmeasure.UnitCode;
import naakcii.by.api.unitofmeasure.UnitOfMeasure;
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

import naakcii.by.api.category.Category;
import naakcii.by.api.chain.Chain;
import naakcii.by.api.chainproduct.ChainProduct;
import naakcii.by.api.chainproduct.ChainProductRepository;
import naakcii.by.api.chainproducttype.ChainProductType;
import naakcii.by.api.product.Product;
import naakcii.by.api.subcategory.Subcategory;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-unit-test.properties")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ChainProductRepositoryTest {
	
	private static Calendar currentDate = Calendar.getInstance();
	
	@Autowired
	private TestEntityManager testEntityManager;
	
	@Autowired
	private ChainProductRepository chainProductRepository;
	
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
		return new GregorianCalendar(currentDate.get(Calendar.YEAR), 
									 currentDate.get(Calendar.MONTH), 
									 currentDate.get(Calendar.DAY_OF_MONTH)
		);
	}

	@Before
	public void setUp() {
		Category category = new Category("Мясо и колбасные изделия", true);
		category.setIcon("Мясо и колбасные изделия.png");
		Subcategory firstSubcategory = new Subcategory("Говядина", true, category);
		Subcategory secondSubcategory = new Subcategory("Колбасные изделия", true, category);
		Subcategory thirdSubcategory = new Subcategory("Копчености", true, category);
		Subcategory fourthSubcategory = new Subcategory("Птица", true, category);
		Chain firstChain = new Chain("Алми", "Almi", "www.almi.by", true);
		Chain secondChain = new Chain("Виталюр", "Vitalur", "www.vitalur.by", true);
		Chain thirdChain = new Chain("Евроопт", "Evroopt", "www.evroopt.by", true);
		Chain fourthChain = new Chain("БелМаркет", "Belmarket","www.belmarket.by", true);
		ChainProductType firstChainProductType = new ChainProductType("Скидка", "discount");
		ChainProductType secondChainProductType = new ChainProductType("Хорошая цена", "good_price");
		UnitOfMeasure firstUnitOfMeasure = new UnitOfMeasure(UnitCode.KG);
		UnitOfMeasure secondUnitOfMeasure = new UnitOfMeasure(UnitCode.PC);
		Product firstProduct = new Product("10000000000000", "Полуфабрикат из говядины", firstUnitOfMeasure, true, firstSubcategory);
		Product secondProduct = new Product("20000000000000", "Котлетное мясо", firstUnitOfMeasure, true, firstSubcategory);
		Product thirdProduct = new Product("30000000000000", "Тазобедренная часть", firstUnitOfMeasure, true, firstSubcategory);
		Product fourthProduct = new Product("40000000000000", "Сервелат", firstUnitOfMeasure, true, secondSubcategory);
		Product fifthProduct = new Product("50000000000000", "Салями", firstUnitOfMeasure, true, secondSubcategory);
		Product sixthProduct = new Product("60000000000000", "Колбаски Охотничьи", secondUnitOfMeasure, true, thirdSubcategory);
		Product seventhProduct = new Product("70000000000000", "Колбаса копченая", secondUnitOfMeasure, true, thirdSubcategory);
		Product eighthProduct = new Product("80000000000000", "Тушка бройлера", secondUnitOfMeasure, true, fourthSubcategory);
		Product ninthProduct = new Product("90000000000000", "Шейная часть", secondUnitOfMeasure, false, firstSubcategory);
		Product tenthProduct = new Product("10000000000000", "Колбаса докторская", secondUnitOfMeasure, false, secondSubcategory);
		testEntityManager.persist(firstUnitOfMeasure);
		testEntityManager.persist(secondUnitOfMeasure);
		testEntityManager.persist(category);
		testEntityManager.persist(firstChain);
		testEntityManager.persist(secondChain);
		testEntityManager.persist(thirdChain);
		testEntityManager.persist(fourthChain);
		testEntityManager.persist(firstChainProductType);
		testEntityManager.persist(secondChainProductType);
		Calendar firstStartDate = getCurrentDate();
		firstStartDate.add(Calendar.DAY_OF_MONTH, -7);
		Calendar firstEndDate = getCurrentDate();
		firstEndDate.add(Calendar.DAY_OF_MONTH, 7);
		firstChainProduct = new ChainProduct(firstProduct, firstChain, new BigDecimal("5.50"), firstChainProductType, firstStartDate, firstEndDate);
		Calendar secondStartDate = getCurrentDate();
		secondStartDate.add(Calendar.DAY_OF_MONTH, -14);
		Calendar secondEndDate = getCurrentDate();
		secondEndDate.add(Calendar.DAY_OF_MONTH, 7);
		secondChainProduct = new ChainProduct(firstProduct, secondChain, new BigDecimal("6.75"), firstChainProductType, secondStartDate, secondEndDate);
		Calendar thirdStartDate = getCurrentDate();
		Calendar thirdEndDate = getCurrentDate();
		thirdEndDate.add(Calendar.DAY_OF_MONTH, 14);
		thirdChainProduct = new ChainProduct(firstProduct, thirdChain, new BigDecimal("2.25"), secondChainProductType, thirdStartDate, thirdEndDate);
		Calendar fourthStartDate = getCurrentDate();
		fourthStartDate.add(Calendar.MONTH, -1);
		Calendar fourthEndDate = getCurrentDate();
		fourthEndDate.add(Calendar.MONTH, 1);
		fourthChainProduct = new ChainProduct(secondProduct, secondChain, new BigDecimal("12.50"), secondChainProductType, fourthStartDate, fourthEndDate);
		Calendar fifthStartDate = getCurrentDate();
		fifthStartDate.add(Calendar.DAY_OF_MONTH, -5);
		Calendar fifthEndDate = getCurrentDate();
		fifthEndDate.add(Calendar.DAY_OF_MONTH, 5);
		fifthChainProduct = new ChainProduct(secondProduct, thirdChain, new BigDecimal("5.25"), secondChainProductType, fifthStartDate, fifthEndDate);
		Calendar sixthStartDate = getCurrentDate();
		sixthStartDate.add(Calendar.DAY_OF_MONTH, -10);
		Calendar sixthEndDate = getCurrentDate();
		sixthEndDate.add(Calendar.DAY_OF_MONTH, 10);
		sixthChainProduct = new ChainProduct(secondProduct, fourthChain, new BigDecimal("15.50"), firstChainProductType, sixthStartDate, sixthEndDate);
		Calendar seventhStartDate = getCurrentDate();
		seventhStartDate.add(Calendar.DAY_OF_MONTH, -15);
		Calendar seventhEndDate = getCurrentDate();
		seventhEndDate.add(Calendar.DAY_OF_MONTH, 15);
		seventhChainProduct = new ChainProduct(thirdProduct, firstChain, new BigDecimal("2.50"), firstChainProductType, seventhStartDate, seventhEndDate);
		Calendar eighthStartDate = getCurrentDate();
		eighthStartDate.add(Calendar.DAY_OF_MONTH, -20);
		Calendar eighthEndDate = getCurrentDate();
		eighthEndDate.add(Calendar.DAY_OF_MONTH, 20);
		eighthChainProduct = new ChainProduct(fourthProduct, secondChain, new BigDecimal("1.75"), firstChainProductType, eighthStartDate, eighthEndDate);
		Calendar ninthStartDate = getCurrentDate();
		ninthStartDate.add(Calendar.DAY_OF_MONTH, -7);
		Calendar ninthEndDate = getCurrentDate();
		ninthEndDate.add(Calendar.DAY_OF_MONTH, 14);
		ninthChainProduct = new ChainProduct(fifthProduct, thirdChain, new BigDecimal("10.75"), secondChainProductType, ninthStartDate, ninthEndDate);
		Calendar tenthStartDate = getCurrentDate();
		tenthStartDate.add(Calendar.DAY_OF_MONTH, -14);
		Calendar tenthEndDate = getCurrentDate();
		tenthEndDate.add(Calendar.DAY_OF_MONTH, 7);
		tenthChainProduct = new ChainProduct(sixthProduct, fourthChain, new BigDecimal("5.50"), secondChainProductType, tenthStartDate, tenthEndDate);
		Calendar eleventhStartDate = getCurrentDate();
		eleventhStartDate.add(Calendar.DAY_OF_MONTH, -5);
		Calendar eleventhEndDate = getCurrentDate();
		eleventhEndDate.add(Calendar.DAY_OF_MONTH, 5);
		eleventhChainProduct = new ChainProduct(seventhProduct, secondChain, new BigDecimal("7.75"), firstChainProductType, eleventhStartDate, eleventhEndDate);
		Calendar twelvethStartDate = getCurrentDate();
		twelvethStartDate.add(Calendar.DAY_OF_MONTH, -21);
		Calendar twelvethEndDate = getCurrentDate();
		twelvethEndDate.add(Calendar.DAY_OF_MONTH, 21);
		twelvethChainProduct = new ChainProduct(seventhProduct, thirdChain, new BigDecimal("1.00"), secondChainProductType, twelvethStartDate, twelvethEndDate);
		Calendar thirteenthStartDate = getCurrentDate();
		thirteenthStartDate.add(Calendar.DAY_OF_MONTH, -7);
		Calendar thirteenthEndDate = getCurrentDate();
		thirteenthEndDate.add(Calendar.DAY_OF_MONTH, 21);
		thirteenthChainProduct = new ChainProduct(eighthProduct, firstChain, new BigDecimal("4.00"), firstChainProductType, thirteenthStartDate, thirteenthEndDate);
		Calendar fourteenthStartDate = getCurrentDate();
		fourteenthStartDate.add(Calendar.DAY_OF_MONTH, -28);
		Calendar fourteenthEndDate = getCurrentDate();
		fourteenthEndDate.add(Calendar.DAY_OF_MONTH, 28);
		fourteenthChainProduct = new ChainProduct(eighthProduct, fourthChain, new BigDecimal("15.00"), secondChainProductType, fourteenthStartDate, fourteenthEndDate);
		Calendar fifteenthStartDate = getCurrentDate();
		fifteenthStartDate.add(Calendar.DAY_OF_MONTH, -15);
		Calendar fifteenthEndDate = getCurrentDate();
		fifteenthEndDate.add(Calendar.DAY_OF_MONTH, 15);
		fifteenthChainProduct = new ChainProduct(ninthProduct, secondChain, new BigDecimal("0.85"), firstChainProductType, fifteenthStartDate, fifteenthEndDate);
		Calendar sixteenthStartDate = getCurrentDate();
		sixteenthStartDate.add(Calendar.DAY_OF_MONTH, -5);
		Calendar sixteenthEndDate = getCurrentDate();
		sixteenthEndDate.add(Calendar.DAY_OF_MONTH, 5);
		sixteenthChainProduct = new ChainProduct(tenthProduct, thirdChain, new BigDecimal("1.15"), secondChainProductType, sixteenthStartDate, sixteenthEndDate);
		testEntityManager.flush();
		firstSubcategoryId = firstSubcategory.getId();
		secondSubcategoryId = secondSubcategory.getId();
		thirdSubcategoryId = thirdSubcategory.getId();
		fourthSubcategoryId = fourthSubcategory.getId();
		firstChainId = firstChain.getId();
		secondChainId = secondChain.getId();
		thirdChainId = thirdChain.getId();
		fourthChainId = fourthChain.getId();
		testEntityManager.clear();
	}
		
	@Test
	public void test_find_all_by_subcategory_id() {
		List<ChainProduct> ChainProducts = chainProductRepository.findByProductIsActiveTrueAndProductSubcategoryIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
				thirdSubcategoryId, Calendar.getInstance(), Calendar.getInstance());
		assertEquals("Number of ChainProducts, that have been found in the database, should be 3: [10thChainProduct, 11thChainProduct, 12thChainProduct].", 3, ChainProducts.size());
		assertTrue("Result list of ChainProducts should contain 10th ChainProduct.", ChainProducts.contains(tenthChainProduct));
		assertTrue("Result list of ChainProducts should contain 11th ChainProduct.", ChainProducts.contains(eleventhChainProduct));
		assertTrue("Result list of ChainProducts should contain 12th ChainProduct.", ChainProducts.contains(twelvethChainProduct));
	}
	
	@Test
	public void test_find_all_by_subcategories_ids() {
		Set<Long> subcategoryIds = new HashSet<>();
		subcategoryIds.add(firstSubcategoryId);
		subcategoryIds.add(secondSubcategoryId);
		List<ChainProduct> ChainProducts = chainProductRepository.findByProductIsActiveTrueAndProductSubcategoryIdInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
				subcategoryIds, Calendar.getInstance(), Calendar.getInstance());
		assertEquals("Number of ChainProducts, that have been found in the database, should be 9: "
				+ "[1stChainProduct, 2ndChainProduct, 3rdChainProduct, 4thChainProduct, 5thChainProduct, 6thChainProduct, 7thChainProduct, 8thChainProduct, 9thChainProduct].", 9, ChainProducts.size());
		assertTrue("Result list of ChainProducts should contain 1st ChainProduct.", ChainProducts.contains(firstChainProduct));
		assertTrue("Result list of ChainProducts should contain 2nd ChainProduct.", ChainProducts.contains(secondChainProduct));
		assertTrue("Result list of ChainProducts should contain 3rd ChainProduct.", ChainProducts.contains(thirdChainProduct));
		assertTrue("Result list of ChainProducts should contain 4th ChainProduct.", ChainProducts.contains(fourthChainProduct));
		assertTrue("Result list of ChainProducts should contain 5th ChainProduct.", ChainProducts.contains(fifthChainProduct));
		assertTrue("Result list of ChainProducts should contain 6th ChainProduct.", ChainProducts.contains(sixthChainProduct));
		assertTrue("Result list of ChainProducts should contain 7th ChainProduct.", ChainProducts.contains(seventhChainProduct));
		assertTrue("Result list of ChainProducts should contain 8th ChainProduct.", ChainProducts.contains(eighthChainProduct));
		assertTrue("Result list of ChainProducts should contain 9th ChainProduct.", ChainProducts.contains(ninthChainProduct));
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
		List<ChainProduct> ChainProducts = chainProductRepository.findByProductIsActiveTrueAndProductSubcategoryIdInAndChainIdInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
				subcategoryIds, chainIds, Calendar.getInstance(), Calendar.getInstance());
		assertEquals("Number of ChainProducts, that have been found in the database, should be 6: "
				+ "[2ndChainProduct, 3rdChainProduct, 4thChainProduct, 5thChainProduct, 8thChainProduct, 9thChainProduct].", 6, ChainProducts.size());
		assertTrue("Result list of ChainProducts should contain 2nd ChainProduct.", ChainProducts.contains(secondChainProduct));
		assertTrue("Result list of ChainProducts should contain 3rd ChainProduct.", ChainProducts.contains(thirdChainProduct));
		assertTrue("Result list of ChainProducts should contain 4th ChainProduct.", ChainProducts.contains(fourthChainProduct));
		assertTrue("Result list of ChainProducts should contain 5th ChainProduct.", ChainProducts.contains(fifthChainProduct));
		assertTrue("Result list of ChainProducts should contain 8th ChainProduct.", ChainProducts.contains(eighthChainProduct));
		assertTrue("Result list of ChainProducts should contain 9th ChainProduct.", ChainProducts.contains(ninthChainProduct));
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
		List<ChainProduct> ChainProducts = chainProductRepository.findByProductIsActiveTrueAndProductSubcategoryIdInAndChainIdInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
				subcategoryIds, chainIds, Calendar.getInstance(), Calendar.getInstance(), pageRequest);
		assertEquals("Number of ChainProducts, that have been found in the database and placed on the first page, should be 4: "
				+ "[8thChainProduct, 3rdChainProduct, 5thChainProduct, 2ndChainProduct].", 4, ChainProducts.size());
		assertTrue("Result list of ChainProducts should contain 8th ChainProduct.", ChainProducts.contains(eighthChainProduct));
		assertTrue("Result list of ChainProducts should contain 3rd ChainProduct.", ChainProducts.contains(thirdChainProduct));
		assertTrue("Result list of ChainProducts should contain 5th ChainProduct.", ChainProducts.contains(fifthChainProduct));
		assertTrue("Result list of ChainProducts should contain 2nd ChainProduct.", ChainProducts.contains(secondChainProduct));
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
		List<ChainProduct> ChainProducts = chainProductRepository.findByProductIsActiveTrueAndProductSubcategoryIdInAndChainIdInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
				subcategoryIds, chainIds, Calendar.getInstance(), Calendar.getInstance(), pageRequest);
		assertEquals("Number ChainProducts, that have been found in the database and placed on the second page, should be 2: "
				+ "[9thChainProduct, 4thChainProduct].", 2, ChainProducts.size());
		assertTrue("Result list of ChainProducts should contain 9th ChainProduct.", ChainProducts.contains(ninthChainProduct));
		assertTrue("Result list of ChainProducts should contain 4th ChainProduct.", ChainProducts.contains(fourthChainProduct));
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
		List<ChainProduct> ChainProducts = chainProductRepository.findByProductIsActiveTrueAndProductSubcategoryIdInAndChainIdInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
				subcategoryIds, chainIds, Calendar.getInstance(), Calendar.getInstance(), pageRequest);
		assertEquals("Number of ChainProducts, that have been found in the database and placed on the second page, should be 0, as all results have been placed on two previous pages.", 0, ChainProducts.size());
	}
	
	@Test
	public void test_count_by_subcategories_ids_and_chain_ids() {
		Set<Long> subcategoryIds = new HashSet<>();
		subcategoryIds.add(firstSubcategoryId);
		subcategoryIds.add(secondSubcategoryId);
		subcategoryIds.add(fourthSubcategoryId);
		Set<Long> chainIds = new HashSet<>();
		chainIds.add(secondChainId);
		chainIds.add(thirdChainId);
		Long numberOfChainProducts = chainProductRepository.countByProductIsActiveTrueAndProductSubcategoryIdInAndChainIdInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
				subcategoryIds, chainIds, Calendar.getInstance(), Calendar.getInstance());
		assertEquals("Number of ChainProducts, that have been found in the database, should be 6: "
				+ "[8thChainProduct, 3rdChainProduct, 5thChainProduct, 2ndChainProduct, 9thChainProduct, 4thChainProduct].", 6L, numberOfChainProducts.longValue());
	}
	
	@After
	public void tearDown() {
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
		fifteenthChainProduct = null;
		sixteenthChainProduct = null;
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
