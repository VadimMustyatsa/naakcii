package naakcii.by.api.chain;

import static org.junit.Assert.assertTrue;

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
@TestPropertySource(locations = "classpath:application-test.properties")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ChainRepositoryTest {
	
	@Autowired
	private TestEntityManager testEntityManager;
	
	@Autowired
	private ChainRepository chainRepository;
	
	private Chain chain;
	
	@Before
	public void setUp() {
		chain = new Chain("Chain name", "Chain link", true);
		chain.setLogo("Chain logo");
		testEntityManager.persistAndFlush(chain);
		testEntityManager.detach(chain);
	}
	
	@Test
	public void test_soft_delete() {
		int numberOfUpdatedRows = chainRepository.softDelete(chain.getId());
		assertTrue("Number of updated rows in the database should be equal to 1, as 1 entity has been modified.", numberOfUpdatedRows == 1);
	}
	
	@Test
	public void test_soft_delete_with_wrong_id() {
		int numberOfUpdatedRows = chainRepository.softDelete(chain.getId() + 10);
		assertTrue("Number of updated rows in the database should be equal to 0, as nothing has been modified.", numberOfUpdatedRows == 0);
	}
	
	@After
	public void tearDown() {
		chain = null;
	}
}
