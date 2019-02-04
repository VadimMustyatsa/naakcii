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

import static org.junit.Assert.assertEquals;


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
    private Country secondCountry;
    private Country thirdCountry;

    @Before
    public void setUp() {
        firstCountry = new Country(CountryCode.AM);
        secondCountry = new Country(CountryCode.BY);
        thirdCountry = new Country(CountryCode.BE);
        testEntityManager.persist(firstCountry);
        testEntityManager.persist(secondCountry);
        testEntityManager.persistAndFlush(thirdCountry);
        testEntityManager.clear();
    }

    @Test
    public void test_find_by_alpha_code2() {
    	Country expectedCountry = secondCountry;
    	Country resultCountry = countryRepository.findByAlphaCode2(secondCountry.getAlphaCode2()).get();
        assertEquals("Result country should be: {name:'Беларусь', alphaCode2:'BY', alphaCode3:'BLR'}.", expectedCountry, resultCountry);
    }
    
    @Test
    public void test_find_by_alpha_code2_and_alpha_code3() {
    	Country expectedCountry = firstCountry;
    	Country resultCountry = countryRepository.findByAlphaCode2AndAlphaCode3(firstCountry.getAlphaCode2(), firstCountry.getAlphaCode3()).get();
        assertEquals("Result country should be: {name:'Армения', alphaCode2:'AM', alphaCode3:'ARM'}.", expectedCountry, resultCountry);
    }

    @After
    public void tearDown() {
        firstCountry = null;
        secondCountry = null;
        thirdCountry = null;
    }
}
