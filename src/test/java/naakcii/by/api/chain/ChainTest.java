package naakcii.by.api.chain;

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

import naakcii.by.api.unitofmeasure.UnitCode;
import naakcii.by.api.unitofmeasure.UnitOfMeasure;
import org.junit.BeforeClass;
import org.junit.Test;

import naakcii.by.api.chain.Chain;
import naakcii.by.api.chainproduct.ChainProduct;
import naakcii.by.api.chainproducttype.ChainProductType;
import naakcii.by.api.product.Product;
import naakcii.by.api.subcategory.Subcategory;

public class ChainTest {

    private static Validator validator;

    @BeforeClass
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public UnitOfMeasure getUnitOfMeasure() {
        return new UnitOfMeasure(UnitCode.KG); }

	public void createChainProducts(Chain chain) {
		Product product = new Product("1000123456789", "Product Name", getUnitOfMeasure(), true, new Subcategory("Subcategory name", true));
		ChainProductType actionType = new ChainProductType("ChainProductType name", "Synonym");
		Calendar startDate = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();
		endDate.add(Calendar.DAY_OF_MONTH, 10);
		ChainProduct firstAction = new ChainProduct(product, chain, new BigDecimal("5.50"), actionType, startDate, endDate);
		ChainProduct secondAction = new ChainProduct(product, chain, new BigDecimal("3.75"), actionType, startDate, endDate);
	}

	public void createInvalidChainProducts(Chain chain) {
		Product product = new Product("1000123456789", "Product Name", getUnitOfMeasure(), true, new Subcategory("Subcategory name", true));
		ChainProductType actionType = new ChainProductType("ChainProductType name", "Synonym");
		Calendar startDate = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();
		endDate.add(Calendar.DAY_OF_MONTH, 10);
		ChainProduct firstAction = new ChainProduct(product, chain, null, actionType, startDate, endDate);
		ChainProduct secondAction = new ChainProduct(product, chain, new BigDecimal("2.00"), null, startDate, endDate);
	}

	@Test
	public void test_chain_name_is_null() {
		Chain chain = new Chain(null, "1+1", "Chain link", true);
		Set<ConstraintViolation<Chain>> constraintViolations = validator.validate(chain);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as chain name is null:", 1, constraintViolations.size());
        assertEquals("Chain's name mustn't be null.", constraintViolations.iterator().next().getMessage());
	}

	@Test
	public void test_chain_name_is_too_short() {
		Chain chain = new Chain("Ch", "1+1", "Chain link", true);
		Set<ConstraintViolation<Chain>> constraintViolations = validator.validate(chain);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as chain name is too short:", 1, constraintViolations.size());
        assertEquals("Chain's name 'Ch' must be between '3' and '25' characters long.", constraintViolations.iterator().next().getMessage());
	}

	@Test
	public void test_chain_trimmed_name_is_too_short() {
		Chain chain = new Chain("  Ch  ", "1+1", "Chain link", true);
		Set<ConstraintViolation<Chain>> constraintViolations = validator.validate(chain);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as chain trimmed name is too short:", 1, constraintViolations.size());
        assertEquals("Chain's name '  Ch  ' must be between '3' and '25' characters long.", constraintViolations.iterator().next().getMessage());
	}

    @Test
    public void test_chain_synonym_is_null() {
        Chain chain = new Chain("Chain name", null, "Chain link", true);
        Set<ConstraintViolation<Chain>> constraintViolations = validator.validate(chain);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as chain synonym is null:", 1, constraintViolations.size());
        assertEquals("Chain's synonym chain mustn't be null.", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void test_chain_synonym_is_too_short() {
        Chain chain = new Chain("Chain name", "11", "Chain link", true);
        Set<ConstraintViolation<Chain>> constraintViolations = validator.validate(chain);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as chain synonym is too short:", 1, constraintViolations.size());
        assertEquals("Chain's synonym '11' must be between '3' and '25' characters long.", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void test_chain_trimmed_synonym_is_too_short() {
        Chain chain = new Chain("Chain name", "   11   ", "Chain link", true);
        Set<ConstraintViolation<Chain>> constraintViolations = validator.validate(chain);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as chain trimmed synonym is too short:", 1, constraintViolations.size());
        assertEquals("Chain's synonym '   11   ' must be between '3' and '25' characters long.", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void test_chain_logo_is_too_long() {
        Chain chain = new Chain("Chain name", "Synonym", "Chain link", true);
        StringBuilder str = new StringBuilder();
        for (int i = 0; i<256; i++) {
            str.append("a");
        }
        chain.setLogo(str.toString());
        Set<ConstraintViolation<Chain>> constraintViolations = validator.validate(chain);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as chain logo is too long:", 1, constraintViolations.size());
        String s = chain.getLogo();
        assertEquals("Path to the logo of the chain '" + s + "' mustn't be more than '255' characters long.", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void test_chain_link_is_null() {
        Chain chain = new Chain("Chain name", "Synonym", null, true);
        Set<ConstraintViolation<Chain>> constraintViolations = validator.validate(chain);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as chain link is null:", 1, constraintViolations.size());
        assertEquals("Chain's link mustn't be null.", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void test_chain_link_is_too_short() {
        Chain chain = new Chain("Chain name", "Synonym", "link", true);
        Set<ConstraintViolation<Chain>> constraintViolations = validator.validate(chain);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as chain link is too short:", 1, constraintViolations.size());
        assertEquals("Chain's link 'link' must be between '10' and '255' characters long.", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void test_chain_trimmed_link_is_too_short() {
        Chain chain = new Chain("Chain name", "Synonym", "   link   ", true);
        Set<ConstraintViolation<Chain>> constraintViolations = validator.validate(chain);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as chain trimmed link is too short:", 1, constraintViolations.size());
        assertEquals("Chain's link '   link   ' must be between '10' and '255' characters long.", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void test_chain_set_of_chainProducts_contains_null_elements() {
        Chain chain = new Chain("Chain name", "Synonym", "link_to_chain", true);
        chain.getChainProducts().add(null);
        createChainProducts(chain);
        Set<ConstraintViolation<Chain>> constraintViolations = validator.validate(chain);
        assertEquals("Expected size of the ConstraintViolation set should be 1, as chain's set of chain products contains null elements:", 1, constraintViolations.size());
        assertEquals("Chain must have list of chainProducts without null elements.", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void test_chain_set_of_chainProducts_contains_invalid_elements() {
        Chain chain = new Chain("Chain name", "Synonym", "link_to_chain", true);
        createInvalidChainProducts(chain);
        chain.getChainProducts().add(null);
        Set<ConstraintViolation<Chain>> constraintViolations = validator.validate(chain);
        assertEquals("Expected size of the ConstraintViolation set should be 3, as chain set of actions contains invalid elements:", 3, constraintViolations.size());
        List<String> messages = constraintViolations.stream()
                .map((ConstraintViolation<Chain> constraintViolation) -> constraintViolation.getMessage())
                .collect(Collectors.toList());
        assertTrue(messages.contains("ChainProduct's discount price mustn't be null."));
        assertTrue(messages.contains("ChainProduct must have type."));
        assertTrue(messages.contains("ChainProduct's discount price mustn't be null."));
    }


	@Test
	public void test_chain_isActive_field_is_null() {
		Chain chain = new Chain("Chain name", "Synonym", "link_to_chain", null);
		Set<ConstraintViolation<Chain>> constraintViolations = validator.validate(chain);
		assertEquals("Expected size of the ConstraintViolation set should be 1, as chain 'isActive' field is null:", 1, constraintViolations.size());
        assertEquals("Chain must have field 'isActive' defined.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void test_chain_is_valid() {
		Chain chain = new Chain("Chain name", "Synonym", "link_to_chain", true);
		createChainProducts(chain);
		Set<ConstraintViolation<Chain>> constraintViolations = validator.validate(chain);
		assertEquals("Expected size of the ConstraintViolation set should be 0, as chain is valid:", 0, constraintViolations.size());
	}
}
