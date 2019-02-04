package naakcii.by.api.chainproducttype;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-unit-test.properties")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ChainProductTypeRepositoryTest {

    @Autowired
    private ChainProductTypeRepository chainProductTypeRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private ChainProductType firstChainProductType;
    private ChainProductType secondChainProductType;
    private ChainProductType thirdChainProductType;

    @Before
    public void setUp() {
    	firstChainProductType = new ChainProductType("Скидка", "discount");
    	secondChainProductType = new ChainProductType("Хорошая цена", "good_price");
    	thirdChainProductType = new ChainProductType("1+1", "one_plus_one");
        testEntityManager.persist(firstChainProductType);
        testEntityManager.persist(secondChainProductType);
        testEntityManager.persistAndFlush(thirdChainProductType);
        testEntityManager.clear();
    }

    @Test
    public void test_find_by_synonym() {
    	ChainProductType expectedChainProductType = firstChainProductType;
        ChainProductType resultChainProductType = chainProductTypeRepository.findBySynonym(firstChainProductType.getSynonym()).get();
        assertEquals("Result chainProductType should be: {name:'Скидка', synonym:'discount'}.", expectedChainProductType, resultChainProductType);
    }
    
    @Test
    public void test_find_by_name_and_synonym() {
    	ChainProductType expectedChainProductType = thirdChainProductType;
        ChainProductType resultChainProductType = chainProductTypeRepository.findByNameAndSynonym(thirdChainProductType.getName(), thirdChainProductType.getSynonym()).get();
        assertEquals("Result chainProductType should be: {name:'1+1', synonym:'one_plus_one'}.", expectedChainProductType, resultChainProductType);
    }

    @After
    public void tearDown() {
    	firstChainProductType = null;
    	secondChainProductType = null;
    	thirdChainProductType = null;
    }
}
