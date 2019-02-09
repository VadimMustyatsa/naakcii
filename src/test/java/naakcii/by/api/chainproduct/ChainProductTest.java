package naakcii.by.api.chainproduct;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.BeforeClass;
import org.junit.Test;

import naakcii.by.api.chain.Chain;
import naakcii.by.api.chainproduct.ChainProduct;
import naakcii.by.api.chainproducttype.ChainProductType;
import naakcii.by.api.product.Product;

public class ChainProductTest {

	private static Validator validator;
	
	@BeforeClass
	public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
	
	public ChainProductType getChainProductType() {
		return new ChainProductType("Скидка", "discount");
	}
	
	public Calendar getStartDate() {
		return Calendar.getInstance();
	}
	
	public Calendar getEndDate() {
		Calendar endDate = Calendar.getInstance();
		endDate.add(Calendar.MONTH, 1);
		return endDate;
	}
	
	@Test
	public void test_chain_product_base_price_has_too_many_fraction_digits() {
		ChainProduct chainProduct = new ChainProduct(new Product(), new Chain(), new BigDecimal("5.25"), getChainProductType(), getStartDate(), getEndDate());
		chainProduct.setBasePrice(new BigDecimal("10.575"));
		Set<ConstraintViolation<ChainProduct>> constraintViolations = validator.validate(chainProduct);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as chainProduct price has too many fraction digits:", 1, constraintViolations.size());
        assertEquals("ChainProduct's base price '10.575' must have up to '2' integer digits and '2' fraction digits.", constraintViolations.iterator().next().getMessage());
	}

	@Test
	public void test_chain_product_base_price_is_too_high() {
		ChainProduct chainProduct = new ChainProduct(new Product(), new Chain(), new BigDecimal("5.25"), getChainProductType(), getStartDate(), getEndDate());
        chainProduct.setBasePrice(new BigDecimal("77.50"));
		Set<ConstraintViolation<ChainProduct>> constraintViolations = validator.validate(chainProduct);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as chainProduct price is too high:", 1, constraintViolations.size());
        assertEquals("ChainProduct's base price '77.50' must be lower than '75'.", constraintViolations.iterator().next().getMessage());
	}

	@Test
	public void test_chain_product_base_price_is_too_low() {
		ChainProduct chainProduct = new ChainProduct(new Product(), new Chain(), new BigDecimal("5.25"), getChainProductType(), getStartDate(), getEndDate());
		chainProduct.setBasePrice(new BigDecimal("0.15"));
		Set<ConstraintViolation<ChainProduct>> constraintViolations = validator.validate(chainProduct);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as chainProduct price is too low:", 1, constraintViolations.size());
        assertEquals("ChainProduct's base price '0.15' must be higher than '0.20'.", constraintViolations.iterator().next().getMessage());
	}

    @Test
    public void test_chain_product_discount_price_is_null() {
        ChainProduct chainProduct = new ChainProduct(new Product(), new Chain(), null, getChainProductType(), getStartDate(), getEndDate());
        Set<ConstraintViolation<ChainProduct>> constraintViolations = validator.validate(chainProduct);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as chainProduct discount price is null:", 1, constraintViolations.size());
        assertEquals("ChainProduct's discount price mustn't be null.", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void test_chain_product_discount_price_has_too_many_fraction_digits() {
        ChainProduct chainProduct = new ChainProduct(new Product(), new Chain(), new BigDecimal("5.575"), getChainProductType(), getStartDate(), getEndDate());
        Set<ConstraintViolation<ChainProduct>> constraintViolations = validator.validate(chainProduct);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as chainProduct discount price has too many fraction digits:", 1, constraintViolations.size());
        assertEquals("ChainProduct's discount price '5.575' must have up to '2' integer digits and '2' fraction digits.", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void test_chain_product_discount_price_is_too_high() {
        ChainProduct chainProduct = new ChainProduct(new Product(), new Chain(), new BigDecimal("55.50"), getChainProductType(), getStartDate(), getEndDate());
        Set<ConstraintViolation<ChainProduct>> constraintViolations = validator.validate(chainProduct);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as chainProduct discount price is too high:", 1, constraintViolations.size());
        assertEquals("ChainProduct's discount price '55.50' must be lower than '50'.", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void test_chain_product_discount_price_is_too_low() {
        ChainProduct chainProduct = new ChainProduct(new Product(), new Chain(), new BigDecimal("0.05"), getChainProductType(), getStartDate(), getEndDate());
        Set<ConstraintViolation<ChainProduct>> constraintViolations = validator.validate(chainProduct);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as chainProduct discount price is too low:", 1, constraintViolations.size());
        assertEquals("ChainProduct's discount price '0.05' must be higher than '0.20'.", constraintViolations.iterator().next().getMessage());
    }

    @Test
	public void test_chain_product_discount_percent_has_too_many_fraction_digits() {
		ChainProduct chainProduct = new ChainProduct(new Product(), new Chain(), new BigDecimal("5.25"), getChainProductType(), getStartDate(), getEndDate());
		chainProduct.setDiscountPercent(new BigDecimal("25.50"));
		Set<ConstraintViolation<ChainProduct>> constraintViolations = validator.validate(chainProduct);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as chainProduct discount has too many fraction digits:", 1, constraintViolations.size());
        assertEquals("ChainProduct's discount percent '25.50' must have up to '2' integer digits and '0' fraction digits.", constraintViolations.iterator().next().getMessage());
	}

	@Test
	public void test_chain_product_discount_percent_is_too_high() {
		ChainProduct chainProduct = new ChainProduct(new Product(), new Chain(), new BigDecimal("5.25"), getChainProductType(), getStartDate(), getEndDate());
		chainProduct.setDiscountPercent(new BigDecimal("75"));
		Set<ConstraintViolation<ChainProduct>> constraintViolations = validator.validate(chainProduct);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as chainProduct discount is too high:", 1, constraintViolations.size());
        assertEquals("ChainProduct's discount percent '75' must be lower than '50'.", constraintViolations.iterator().next().getMessage());
	}

	@Test
	public void test_chain_product_discount_percent_is_negative() {
		ChainProduct chainProduct = new ChainProduct(new Product(), new Chain(), new BigDecimal("5.25"), getChainProductType(), getStartDate(), getEndDate());
		chainProduct.setDiscountPercent(new BigDecimal("-10"));
		Set<ConstraintViolation<ChainProduct>> constraintViolations = validator.validate(chainProduct);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as chainProduct discount is negative:", 1, constraintViolations.size());
        assertEquals("ChainProduct's discount percent '-10' mustn't be negative.", constraintViolations.iterator().next().getMessage());
	}

    @Test
    public void test_chain_product_start_date_is_null() {
        ChainProduct chainProduct = new ChainProduct(new Product(), new Chain(), new BigDecimal("15.25"), getChainProductType(), null, getEndDate());
        Set<ConstraintViolation<ChainProduct>> constraintViolations = validator.validate(chainProduct);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as chainProduct start date is null:", 1, constraintViolations.size());
        assertEquals("ChainProduct must have have start date.", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void test_chain_product_end_date_is_null() {
        ChainProduct chainProduct = new ChainProduct(new Product(), new Chain(), new BigDecimal("15.25"), getChainProductType(), getStartDate(), null);
        Set<ConstraintViolation<ChainProduct>> constraintViolations = validator.validate(chainProduct);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as chainProduct end date is null:", 1, constraintViolations.size());
        assertEquals("ChainProduct must have end date.", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void test_chain_product_end_date_is_in_the_past() {
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, -1);
        ChainProduct chainProduct = new ChainProduct(new Product(), new Chain(), new BigDecimal("5.25"), getChainProductType(), getStartDate(), endDate);
        Set<ConstraintViolation<ChainProduct>> constraintViolations = validator.validate(chainProduct);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as chainProduct end date is in the past:", 1, constraintViolations.size());
        assertEquals("ChainProduct's end date must be in the future.", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void test_chain_product_has_no_product() {
        ChainProduct chainProduct = new ChainProduct();
        chainProduct.setChain(new Chain());
        chainProduct.setDiscountPrice(new BigDecimal("5.25"));
        chainProduct.setType(getChainProductType());
        chainProduct.setStartDate(getStartDate());
        chainProduct.setEndDate(getEndDate());
        Set<ConstraintViolation<ChainProduct>> constraintViolations = validator.validate(chainProduct);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as chainProduct has no product:", 1, constraintViolations.size());
        assertEquals("ChainProduct must have product.", constraintViolations.iterator().next().getMessage());
    }

    @Test
	public void test_chain_product_has_no_chain() {
		ChainProduct chainProduct = new ChainProduct();
		chainProduct.setProduct(new Product());
		chainProduct.setDiscountPrice(new BigDecimal("5.25"));
		chainProduct.setType(getChainProductType());
		chainProduct.setStartDate(getStartDate());
		chainProduct.setEndDate(getEndDate());
		Set<ConstraintViolation<ChainProduct>> constraintViolations = validator.validate(chainProduct);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as chainProduct has no chain:", 1, constraintViolations.size());
        assertEquals("ChainProduct must have chain.", constraintViolations.iterator().next().getMessage());
	}

	@Test
	public void test_chain_product_type_is_null() {
		ChainProduct chainProduct = new ChainProduct(new Product(), new Chain(), new BigDecimal("5.25"), null, getStartDate(), getEndDate());
		Set<ConstraintViolation<ChainProduct>> constraintViolations = validator.validate(chainProduct);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as chainProduct type is null:", 1, constraintViolations.size());
        assertEquals("ChainProduct must have type.", constraintViolations.iterator().next().getMessage());
	}

	@Test
	public void test_chain_product_type_is_invalid() {
		ChainProductType chainProductType = new ChainProductType();
		ChainProduct chainProduct = new ChainProduct(new Product(), new Chain(), new BigDecimal("5.25"), chainProductType, getStartDate(), getEndDate());
		Set<ConstraintViolation<ChainProduct>> constraintViolations = validator.validate(chainProduct);
		assertEquals("Expected size of the ConstraintViolation set should be 2, as chainProduct type is invalid:", 2, constraintViolations.size());
        List<String> messages = constraintViolations.stream()
                .map((ConstraintViolation<ChainProduct> constraintViolation) -> constraintViolation.getMessage())
                .collect(Collectors.toList());
        assertTrue(messages.contains("ChainProductType's name mustn't be null."));
        assertTrue(messages.contains("ChainProductType's synonym mustn't be null."));
	}

	@Test
	public void test_chain_product_is_valid() {
		ChainProduct chainProduct = new ChainProduct(new Product(), new Chain(), new BigDecimal("15.25"), getChainProductType(), getStartDate(), getEndDate());
		chainProduct.setBasePrice(new BigDecimal("20.75"));
		chainProduct.setDiscountPercent(new BigDecimal("10"));
		Set<ConstraintViolation<ChainProduct>> constraintViolations = validator.validate(chainProduct);
		assertEquals("Expected size of the ConstraintViolation set should be 0, as chainProduct is valid:", 0, constraintViolations.size());
	}
}
