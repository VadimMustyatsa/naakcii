package naakcii.by.api.chain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-unit-test.properties")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ChainRepositoryTest {

	@Autowired
	private TestEntityManager testEntityManager;
	
	@Autowired
	private ChainRepository chainRepository;
	
	private Chain firstChain;
	private Chain secondChain;
	private Chain thirdChain;
	
	@Before
	public void setUp() {
		firstChain = new Chain("Алми", "Almi", "www.almi.by", true);
		firstChain.setLogo("almi.png");
		secondChain = new Chain("Евроопт", "Evroopt", "www.evroopt.by", true);
		secondChain.setLogo("evroopt.png");
		thirdChain = new Chain("Виталюр", "Vitalur","www.vitalur.by", true);
		thirdChain.setLogo("vitalur.png");
		testEntityManager.persist(firstChain);
		testEntityManager.persist(secondChain);
		testEntityManager.persistAndFlush(thirdChain);
		testEntityManager.clear();
	}

	@Test
	public void test_soft_delete() {
		int numberOfUpdatedRows = chainRepository.softDelete(firstChain.getId());
		assertTrue("Number of updated rows in the database should be equal to 1, as 1 entity has been modified.", numberOfUpdatedRows == 1);
	}

	@Test
	public void test_soft_delete_with_wrong_id() {
		int numberOfUpdatedRows = chainRepository.softDelete(firstChain.getId() + 10);
		assertTrue("Number of updated rows in the database should be equal to 0, as nothing has been modified.", numberOfUpdatedRows == 0);
	}

	@Test
	public void test_get_all_synonyms() {
		List<String> expectedSynonyms = new ArrayList<>();
		expectedSynonyms.add(firstChain.getSynonym());
		expectedSynonyms.add(secondChain.getSynonym());
		expectedSynonyms.add(thirdChain.getSynonym());
		List<String> resultSynonyms = chainRepository.getAllSynonyms();
		assertEquals("Result list of synonyms should contain 3 elements.", expectedSynonyms, resultSynonyms);
		assertTrue("Result list of synonyms should be: ['Almi', 'Evroopt', 'Vitalur'].",
				resultSynonyms.containsAll(expectedSynonyms));
	}
	
	@After
	public void tearDown() {
		firstChain = null;
		secondChain = null;
		thirdChain = null;
	}
}
