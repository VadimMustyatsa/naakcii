package naakcii.by.api.chain;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import naakcii.by.api.APIApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = APIApplication.class)
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ChainControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private TestEntityManager testEntityManager;
	
	private ObjectMapper objectMapper;
	
	@Before
	public void setUp() {
		objectMapper = new ObjectMapper();
	}
	
	private List<Chain> createListOfChains() {
		Chain firstChain = new Chain("First chain", "First chain link", true);
		firstChain.setLogo("First chain logo");
		firstChain.setLogoSmall("First chain small logo");
		Chain secondChain = new Chain("Second chain", "Second chain link", true);
		secondChain.setLogo("Second chain logo");
		secondChain.setLogoSmall("Second chain small logo");
		Chain thirdChain = new Chain("Third chain", "Third chain link", true);
		thirdChain.setLogo("Third chain logo");
		thirdChain.setLogoSmall("Third chain small logo");
		testEntityManager.persist(firstChain);
		testEntityManager.persist(secondChain);
		testEntityManager.persist(thirdChain);
		testEntityManager.detach(firstChain);
		testEntityManager.detach(secondChain);
		testEntityManager.detach(thirdChain);
		List<Chain> chains = new ArrayList<>();
		chains.add(firstChain);
		chains.add(secondChain);
		chains.add(thirdChain);
		return chains;
	}
	
	@Test
	public void test_get_all_chains() throws Exception {
		List<ChainDTO> expectedChainDTOs = createListOfChains()
				.stream()
				.map(ChainDTO::new)
				.collect(Collectors.toList());
		String expectedJson = objectMapper.writeValueAsString(expectedChainDTOs);
		MvcResult mvcResult = this.mockMvc.perform(get("/chain"))
								  .andExpect(status().isOk())
								  .andExpect(content().contentType("application/json;charset=UTF-8"))
								  .andDo(print())
								  .andReturn();
		String resultJson = mvcResult.getResponse().getContentAsString();
		assertEquals("Expected json should be: ["
				   + "{\"id\":1,\"name\":\"First chain\",\"logo\":\"First chain logo\",\"logoSmall\":\"First chain small logo\",\"link\":\"First chain link\"},"
				   + "{\"id\":2,\"name\":\"Second chain\",\"logo\":\"Second chain logo\",\"logoSmall\":\"Second chain small logo\",\"link\":\"Second chain link\"},"
				   + "{\"id\":3,\"name\":\"Third chain\",\"logo\":\"Third chain logo\",\"logoSmall\":\"Third chain small logo\",\"link\":\"Third chain link\"}"
				   + "].", expectedJson, resultJson);					
	}
}
