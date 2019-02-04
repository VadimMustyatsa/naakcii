package naakcii.by.api.product;

import naakcii.by.api.category.Category;
import naakcii.by.api.chain.Chain;
import naakcii.by.api.chainproduct.ChainProduct;
import naakcii.by.api.chainproducttype.ChainProductType;
import naakcii.by.api.country.Country;
import naakcii.by.api.country.CountryCode;
import naakcii.by.api.subcategory.Subcategory;
import naakcii.by.api.unitofmeasure.UnitCode;
import naakcii.by.api.unitofmeasure.UnitOfMeasure;

import org.apache.commons.lang3.StringUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ProductTest {

    private static Validator validator;

    @BeforeClass
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public Subcategory getSubcategory() {
        Category category = new Category("Молочные продукты, яйца", true);
        Subcategory subcategory = new Subcategory("Кисломолочные изделия", true, category);
        return subcategory;
    }

    public Subcategory getInvalidSubcategory() {
        Subcategory subcategory = new Subcategory("Кисломолочные изделия", true);
        return subcategory;
    }

    public void createChainProducts(Product product) {
        Chain chain = new Chain("Алми", "almi", "www.almi.by", true);
        ChainProductType chainProductType = new ChainProductType("Скидка", "discount");
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DAY_OF_MONTH, 15);
        ChainProduct firstChainProduct = new ChainProduct(product, chain, new BigDecimal("2.25"), chainProductType, startDate, endDate);
        ChainProduct secondChainProduct = new ChainProduct(product, chain, new BigDecimal("6.00"), chainProductType, startDate, endDate);
    }
    
    public void createInvalidChainProducts(Product product) {
    	Chain chain = new Chain("Алми", "almi", "www.almi.by", true);
    	ChainProductType chainProductType = new ChainProductType("Скидка", "discount");
		Calendar startDate = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();
		endDate.add(Calendar.DAY_OF_MONTH, 15); 
		ChainProduct firstChainProduct = new ChainProduct(product, chain, new BigDecimal("2.25"), null, startDate, endDate);
		ChainProduct secondChainProduct = new ChainProduct(product, chain, null, chainProductType, startDate, endDate);
	}

    @Test
    public void test_product_barcode_is_null() {
        Product product = new Product(null, "Козинак из арахиса 170г", new UnitOfMeasure(UnitCode.KG), true, getSubcategory());
        createChainProducts(product);
        Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as product barcode is null:", 1, constraintViolations.size());
        assertEquals("Product's bar-code mustn't be null.", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void test_product_barcode_is_too_short() {
        Product product = new Product("123", "Козинак из арахиса 170г", new UnitOfMeasure(UnitCode.KG), true, getSubcategory());
        createChainProducts(product);
        Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as product barcode is too short:", 1, constraintViolations.size());
        assertEquals("Bar-code '123' must be 4, 8, 12, 13 or 14 characters long.", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void test_product_barcode_does_not_contain_only_digits() {
        Product product = new Product(" 100ax586 ", "Козинак из арахиса 170г", new UnitOfMeasure(UnitCode.KG), true, getSubcategory());
        createChainProducts(product);
        Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as product barcode doesn't contain only digits:", 1, constraintViolations.size());
        assertEquals("Bar-code ' 100ax586 ' must consist only of digits.", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void test_product_name_is_null() {
        Product product = new Product("4620004251220", null, new UnitOfMeasure(UnitCode.KG), true, getSubcategory());
        createChainProducts(product);
        Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as product name is null:", 1, constraintViolations.size());
        assertEquals("Product's name mustn't be null.", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void test_product_name_is_too_short() {
        Product product = new Product("4620004251220", "Pr", new UnitOfMeasure(UnitCode.KG), true, getSubcategory());
        createChainProducts(product);
        Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as product name is too short:", 1, constraintViolations.size());
        assertEquals("Product's name 'Pr' must be between '3' and '100' characters long.", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void test_product_trimmed_name_is_too_short() {
        Product product = new Product("4620004251220", "  Pr  ", new UnitOfMeasure(UnitCode.KG), true, getSubcategory());
        createChainProducts(product);
        Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as product trimmed name is too short:", 1, constraintViolations.size());
        assertEquals("Product's name '  Pr  ' must be between '3' and '100' characters long.", constraintViolations.iterator().next().getMessage());
    }
    
    @Test
    public void test_product_picture_is_too_long() {
        Product product = new Product("4620004251220", "Козинак из арахиса 170г", new UnitOfMeasure(UnitCode.KG), true, getSubcategory());
        createChainProducts(product);
        String picture = StringUtils.repeat("path_to_the_picture", "/", 25);
        product.setPicture(picture);
        Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as path to the picture of the product is too long:", 1, constraintViolations.size());
        assertEquals("Path to the picture of the product '" + picture + "' mustn't be more than '255' characters long.", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void test_product_unit_of_measure_is_null() {
        Product product = new Product("4620004251220", "Козинак из арахиса 170г", null, true, getSubcategory());
        createChainProducts(product);
        Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as unit of measure is null:", 1, constraintViolations.size());
        assertEquals("Product's unit of measure mustn't be null.", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void test_product_manufacturer_is_too_long() {
        Product product = new Product("4620004251220", "Козинак из арахиса 170г", new UnitOfMeasure(UnitCode.KG), true, getSubcategory());
        createChainProducts(product);
        String manufacturer = StringUtils.repeat("Азовская кондитерская фабрика", "; ", 5);
        product.setManufacturer(manufacturer);
        Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as name of the manufacturer is too long:", 1, constraintViolations.size());
        assertEquals("Product's manufacturer '" + manufacturer + "' mustn't be more than '50' characters long.", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void test_product_brand_is_too_long() {
        Product product = new Product("4620004251220", "Козинак из арахиса 170г", new UnitOfMeasure(UnitCode.KG), true, getSubcategory());
        createChainProducts(product);
        String brand = StringUtils.repeat("АЗОВСКАЯ КОНДИТЕРСКАЯ ФАБРИКА", "; ", 5);
        product.setBrand(brand);
        Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as brand is too long:", 1, constraintViolations.size());
        assertEquals("Product's brand '" + brand + "' mustn't be more than '50' characters long.", constraintViolations.iterator().next().getMessage());
    }
    
    @Test
	public void test_product_has_invalid_country_of_origin() {
    	Product product = new Product("4620004251220", "Козинак из арахиса 170г", new UnitOfMeasure(UnitCode.KG), true, getSubcategory());
    	createChainProducts(product);
		Country country = new Country();
		country.setAlphaCode2("BY");
		country.setAlphaCode3("BLR");
		product.setCountryOfOrigin(country);
		Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as product has invalid country of origin:", 1, constraintViolations.size());
        assertEquals("Country's name mustn't be null.", constraintViolations.iterator().next().getMessage());
	}

    @Test
    public void test_product_does_not_belong_to_any_subcategory() {
        Product product = new Product("4620004251220", "Козинак из арахиса 170г", new UnitOfMeasure(UnitCode.KG), true);
        createChainProducts(product);
        Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as subcategory is null:", 1, constraintViolations.size());
        assertEquals("Product must have subcategory.", constraintViolations.iterator().next().getMessage());
    }
    
    @Test
	public void test_product_belongs_to_invalid_subcategory() {
    	Product product = new Product("4620004251220", "Козинак из арахиса 170г", new UnitOfMeasure(UnitCode.KG), true, getInvalidSubcategory());
    	createChainProducts(product);
		Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as product belongs to subcategory with null 'category' field:", 1, constraintViolations.size());
        assertEquals("Subcategory must have category.", constraintViolations.iterator().next().getMessage());
	}
    
    @Test
	public void test_product_set_of_chainProducts_contains_invalid_elements() {
    	Product product = new Product("4620004251220", "Product name", new UnitOfMeasure(UnitCode.KG), false, getSubcategory());
    	createInvalidChainProducts(product);
		product.getChainProducts().add(null);
		Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
		assertEquals("Expected size of the ConstraintViolation set should be 3, as product set of chain products contains invalid elements:", 3, constraintViolations.size());
		List<String> messages = constraintViolations.stream()
				.map((ConstraintViolation<Product> constraintViolation) -> constraintViolation.getMessage())
				.collect(Collectors.toList());
		assertTrue(messages.contains("ChainProduct's discount price mustn't be null."));
		assertTrue(messages.contains("ChainProduct must have type."));
		assertTrue(messages.contains("Product must have list of chainProducts without null elements."));
	}

    @Test
	public void test_product_is_valid() {
    	Product product = new Product("4620004251220", "Product name", new UnitOfMeasure(UnitCode.KG), false, getSubcategory());
    	createChainProducts(product);
		product.setPicture("D:/products/pictures/sweets/nut_and_honey_bar.jpg");
		product.setManufacturer("Азовская кондитерская фабрика");
		product.setBrand("АЗОВСКАЯ КОНДИТЕРСКАЯ ФАБРИКА");
		product.setCountryOfOrigin(new Country(CountryCode.RU));
		Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as product is valid:", 0, constraintViolations.size());
	}
}
