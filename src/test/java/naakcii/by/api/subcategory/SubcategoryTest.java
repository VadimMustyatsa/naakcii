package naakcii.by.api.subcategory;

import naakcii.by.api.category.Category;
import naakcii.by.api.chain.Chain;
import naakcii.by.api.chainproduct.ChainProduct;
import naakcii.by.api.chainproducttype.ChainProductType;
import naakcii.by.api.product.Product;
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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SubcategoryTest {

    private static Validator validator;

    @BeforeClass
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public Category getCategory(Subcategory subcategory) {
        Category category = new Category("Овощи и фрукты", true);
        category.getSubcategories().add(subcategory);
        return category;
    }

    public Category getInvalidCategory(Subcategory subcategory) {
        Category category = new Category("Овощи и фрукты", null);
        category.getSubcategories().add(subcategory);
        return category;
    }

    public void createProducts(Subcategory subcategory) {
        Chain chain = new Chain("ProStore", "prostore", "www.prostore.by", true);
        Product firstProduct = new Product("2102354000000", "Киви Импорт Вес", new UnitOfMeasure(UnitCode.KG), true, subcategory);
        Product secondProduct = new Product("2100220000000", "Апельсины крупные Импорт Вес", new UnitOfMeasure(UnitCode.KG), true, subcategory);
        ChainProductType chainProductType = new ChainProductType("Скидка", "discount");
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DAY_OF_MONTH, 5);
        ChainProduct firstAction = new ChainProduct(firstProduct, chain, new BigDecimal("3.39"), chainProductType, startDate, endDate);
        ChainProduct secondAction = new ChainProduct(secondProduct, chain, new BigDecimal("3.19"), chainProductType, startDate, endDate);
    }

    public void createInvalidProducts(Subcategory subcategory) {
        Chain chain = new Chain("ProStore", "prostore", "www.prostore.by", true);
        Product firstProduct = new Product("2102354000000", null, new UnitOfMeasure(UnitCode.KG), true, subcategory);
        Product secondProduct = new Product("2100220000000", "Апельсины крупные Импорт Вес", new UnitOfMeasure(UnitCode.KG), null, subcategory);
        ChainProductType chainProductType = new ChainProductType("Скидка", "discount");
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DAY_OF_MONTH, 5);
        ChainProduct firstAction = new ChainProduct(firstProduct, chain, new BigDecimal("3.39"), chainProductType, startDate, endDate);
        ChainProduct secondAction = new ChainProduct(secondProduct, chain, new BigDecimal("3.19"), chainProductType, startDate, endDate);
    }

    @Test
    public void test_subcategory_name_is_null() {
        Subcategory subcategory = new Subcategory(null, true);
        subcategory.setCategory(getCategory(subcategory));
        Set<ConstraintViolation<Subcategory>> constraintViolations = validator.validate(subcategory);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as subcategory's name is null:", 1, constraintViolations.size());
        assertEquals("Subcategory's name mustn't be null.", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void test_subcategory_name_too_short() {
        Subcategory subcategory = new Subcategory("Фр", true);
        subcategory.setCategory(getCategory(subcategory));
        Set<ConstraintViolation<Subcategory>> constraintViolations = validator.validate(subcategory);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as subcategory's name is too short:", 1, constraintViolations.size());
        assertEquals("Subcategory's name 'Фр' must be between '3' and '50' characters long.", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void test_subcategory_trimmed_name_too_short() {
        Subcategory subcategory = new Subcategory("  Фр  ", true);
        subcategory.setCategory(getCategory(subcategory));
        Set<ConstraintViolation<Subcategory>> constraintViolations = validator.validate(subcategory);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as subcategory trimmed name is too short:", 1, constraintViolations.size());
        assertEquals("Subcategory's name '  Фр  ' must be between '3' and '50' characters long.", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void test_subcategory_priority_is_negative() {
        Subcategory subcategory = new Subcategory("Фрукты", true);
        subcategory.setCategory(getCategory(subcategory));
        subcategory.setPriority(-1);
        Set<ConstraintViolation<Subcategory>> constraintViolations = validator.validate(subcategory);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as subcategory's priority is negative:", 1, constraintViolations.size());
        assertEquals("Subcategory's priority '-1' must be positive.", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void test_subcategory_isActive_field_is_null() {
        Subcategory subcategory = new Subcategory("Фрукты", null);
        subcategory.setCategory(getCategory(subcategory));
        Set<ConstraintViolation<Subcategory>> constraintViolations = validator.validate(subcategory);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as subcategory's 'isActive' field is null:", 1, constraintViolations.size());
        assertEquals("Subcategory must have field 'isActive' defined.", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void test_subcategory_does_not_belong_to_any_category() {
        Subcategory subcategory = new Subcategory("Фрукты", false);
        Set<ConstraintViolation<Subcategory>> constraintViolations = validator.validate(subcategory);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as subcategory doesn't belong to any category:", 1, constraintViolations.size());
        assertEquals("Subcategory must have category.", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void test_subcategory_belongs_to_invalid_category() {
        Subcategory subcategory = new Subcategory("Фрукты", false);
        subcategory.setCategory(getInvalidCategory(subcategory));
        Set<ConstraintViolation<Subcategory>> constraintViolations = validator.validate(subcategory);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as subcategory belongs to category with null 'isActive' field:", 1, constraintViolations.size());
        assertEquals("Category must have field 'isActive' defined.", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void test_subcategory_set_of_products_contains_invalid_elements() {
        Subcategory subcategory = new Subcategory("Фрукты", true);
        subcategory.setCategory(getCategory(subcategory));
        createInvalidProducts(subcategory);
        subcategory.getProducts().add(null);
        Set<ConstraintViolation<Subcategory>> constraintViolations = validator.validate(subcategory);
        assertEquals("Expected size of the ConstraintViolation set should be 2, as subcategory's set of products contains invalid elements:", 3, constraintViolations.size());
        List<String> messages = constraintViolations.stream()
                .map((ConstraintViolation<Subcategory> constraintViolation) -> constraintViolation.getMessage())
                .collect(Collectors.toList());
        assertTrue(messages.contains("Product must have field 'isActive' defined."));
        assertTrue(messages.contains("Product's name mustn't be null."));
        assertTrue(messages.contains("Subcategory must have list of products without null elements."));
    }

    @Test
    public void test_subcategory_is_valid() {
        Subcategory subcategory = new Subcategory("Фрукты", true);
        subcategory.setCategory(getCategory(subcategory));
        createProducts(subcategory);
        subcategory.setPriority(5);
        Set<ConstraintViolation<Subcategory>> constraintViolations = validator.validate(subcategory);
        assertEquals("Expected size of the ConstraintViolation set should be 0, as subcategory is valid:", 0, constraintViolations.size());
    }
}
