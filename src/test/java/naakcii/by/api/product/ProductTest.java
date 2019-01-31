package naakcii.by.api.product;

import naakcii.by.api.category.Category;
import naakcii.by.api.chain.Chain;
import naakcii.by.api.chainproduct.ChainProduct;
import naakcii.by.api.chainproducttype.ChainProductType;
import naakcii.by.api.subcategory.Subcategory;
import naakcii.by.api.unitofmeasure.UnitCode;
import naakcii.by.api.unitofmeasure.UnitOfMeasure;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class ProductTest {

    private static Validator validator;

    @BeforeClass
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public UnitOfMeasure getUnitOfMeasure() {
        return new UnitOfMeasure(UnitCode.KG);
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

    public void createActions(Product product) {
        Chain chain = new Chain("Алми", "almi", "www.almi.by", true);
        ChainProductType actionType = new ChainProductType("Скидка", "discount");
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DAY_OF_MONTH, 15);
        ChainProduct firstAction = new ChainProduct(product, chain, new BigDecimal("2.25"), actionType, startDate, endDate);
        ChainProduct secondAction = new ChainProduct(product, chain, new BigDecimal("6.00"), actionType, startDate, endDate);
    }

    @Test
    public void test_product_barcode_is_null() {
        Product product = new Product(null, "Козинак из арахиса 170г", getUnitOfMeasure(), true, getSubcategory());
        Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as product barcode is null:", 1, constraintViolations.size());
        assertEquals("Product's bar-code mustn't be null.", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void test_product_barcode_is_too_short() {
        Product product = new Product("123", "Козинак из арахиса 170г", getUnitOfMeasure(), true, getSubcategory());
        Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as product barcode is too short:", 1, constraintViolations.size());
        assertEquals("Product's bar-code '123' must be between '4' and '14' characters long.", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void test_product_barcode_does_not_contain_only_digits() {
        Product product = new Product("  100ax", "Козинак из арахиса 170г", getUnitOfMeasure(), true, getSubcategory());
        Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as product barcode doesn't contain only digits:", 1, constraintViolations.size());
        assertEquals("Product's bar-code must contain only digits.", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void test_product_name_is_null() {
        Product product = new Product("1000123456789", null, getUnitOfMeasure(), true, getSubcategory());
        Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as product name is null:", 1, constraintViolations.size());
        assertEquals("Product's name mustn't be null.", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void test_product_name_is_too_short() {
        Product product = new Product("1000123456789", "Pr", getUnitOfMeasure(), true, getSubcategory());
        Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as product name is too short:", 1, constraintViolations.size());
        assertEquals("Product's name 'Pr' must be between '3' and '100' characters long.", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void test_product_trimmed_name_is_too_short() {
        Product product = new Product("1000123456789", "  Pr  ", getUnitOfMeasure(), true, getSubcategory());
        Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as product trimmed name is too short:", 1, constraintViolations.size());
        assertEquals("Product's name '  Pr  ' must be between '3' and '100' characters long.", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void test_unit_of_measure_is_null() {
        Product product = new Product("1000123456789", "Козинак из арахиса 170г", null, true, getSubcategory());
        Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as unit of measure is null:", 1, constraintViolations.size());
        assertEquals("Product's unit of measure mustn't be null.", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void test_manufacturer_is_too_large() {
        Product product = new Product("1000123456789", "Козинак из арахиса 170г", getUnitOfMeasure(), true, getSubcategory());
        product.setManufacturer("Азовская кондитерская фабрика Азовская кондитерская фабрика");
        Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as manufacturer is too large:", 1, constraintViolations.size());
        assertEquals("Product's manufacturer 'Азовская кондитерская фабрика Азовская кондитерская фабрика' mustn't be more than '50' characters long.", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void test_brand_is_too_large() {
        Product product = new Product("1000123456789", "Козинак из арахиса 170г", getUnitOfMeasure(), true, getSubcategory());
        product.setBrand("Азовская кондитерская фабрика Азовская кондитерская фабрика");
        Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as brand is too large:", 1, constraintViolations.size());
        assertEquals("Product's brand 'Азовская кондитерская фабрика Азовская кондитерская фабрика' mustn't be more than '50' characters long.", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void test_subcategory_is_null() {
        Product product = new Product("1000123456789", "Козинак из арахиса 170г", getUnitOfMeasure(), true);
        Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as subcategory is null:", 1, constraintViolations.size());
        assertEquals("Product must have subcategory.", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void test_chain_products_set_invalid_elements() {
        Product product = new Product("1000123456789", "Product name", getUnitOfMeasure(), false, getSubcategory());
        Set<ChainProduct> chainProductSet = new HashSet<>();
        chainProductSet.add(null);
        product.setChainProducts(chainProductSet);
        Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as chainProduct set contains invalid elements:", 1, constraintViolations.size());
        assertEquals("Product must have list of chainProducts without null elements.", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void test_product_isActive_is_null() {
        Product product = new Product("1000123456789", "Product name", getUnitOfMeasure(), null, getSubcategory());
        Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as product 'isActive' field is null:", 1, constraintViolations.size());
        assertEquals("Product must have field 'isActive' defined.", constraintViolations.iterator().next().getMessage());
    }
}
