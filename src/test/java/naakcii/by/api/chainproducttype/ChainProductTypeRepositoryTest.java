package naakcii.by.api.chainproducttype;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

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

    private ChainProductType chainProductType;

    @Before
    public void setUp() {
        chainProductType = new ChainProductType("Скидка", "discount");
        chainProductType.setTooltip("First tooltip");
        testEntityManager.persist(chainProductType);
        testEntityManager.flush();
    }

    @Test
    public void test_find_by_name() {
        Optional<ChainProductType> optionalChainProductType = chainProductTypeRepository.findByName(chainProductType.getName());
        assertTrue("Number of chainProductType's  in the database should be 1", optionalChainProductType.isPresent());
        assertEquals("Скидка", optionalChainProductType.get().getName());
    }

    @After
    public void tearDown() {
        chainProductType=null;
    }
}