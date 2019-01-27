package naakcii.by.api.country;

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

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-unit-test.properties")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CountryRepositoryTest {

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private Country firstCountry;

    @Before
    public void setUp() {
        firstCountry = new Country(CountryCode.AM);
        Country secondCountry = new Country(CountryCode.BY);
        testEntityManager.persist(firstCountry);
        testEntityManager.persist(secondCountry);
        testEntityManager.flush();
        testEntityManager.detach(firstCountry);
    }

    @Test
    public void test_find_by_alpha_code2() {
        Optional<Country> countryOptional = countryRepository.findByAlphaCode2(firstCountry.getAlphaCode2());
        assertTrue("Number of country's  in the database should be", countryOptional.isPresent());
        assertEquals("Армения", countryOptional.get().getName());
    }

    @After
    public void tearDown() {
        firstCountry = null;
    }
}
