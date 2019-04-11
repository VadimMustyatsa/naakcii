package naakcii.by.api.chain;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
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
import org.springframework.util.StopWatch;

import com.fasterxml.jackson.databind.ObjectMapper;

import naakcii.by.api.APIApplication;
import naakcii.by.api.config.ApiConfigConstants;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = APIApplication.class)
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
@Transactional
@TestPropertySource(locations = "classpath:application-integration-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ChainControllerIntegrationTest {

	private static final Logger logger = LogManager.getLogger(ChainControllerIntegrationTest.class);

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private TestEntityManager testEntityManager;
	
	private ObjectMapper objectMapper;
	private StopWatch stopWatch;
	private List<Chain> activeChains;
	private List<Chain> inactiveChains;
	
	@Before
	public void setUp() {
		objectMapper = new ObjectMapper();
		stopWatch = new StopWatch();
		activeChains = new ArrayList<>();
		inactiveChains = new ArrayList<>();
	}
	
	private void createListOfChains() {
		logger.info("Preparing of test data.");
		Chain firstChain = new Chain("Алми", "Almi", "www.almi.by", true);
		firstChain.setLogo("almi.png");
		Chain secondChain = new Chain("Виталюр", "Vitalur", "www.vitalur.by", true);
		secondChain.setLogo("vitalur.png");
		Chain thirdChain = new Chain("Евроопт", "Evroopt", "www.evroopt.by", true);
		thirdChain.setLogo("evroopt.png");
		Chain fourthChain = new Chain("БелМаркет", "Belmarket", "www.belmarket.by", false);
		fourthChain.setLogo("belmarket.png");
		
		try {
			activeChains.add(testEntityManager.persist(firstChain));
			activeChains.add(testEntityManager.persist(secondChain));
			activeChains.add(testEntityManager.persist(thirdChain));
			inactiveChains.add(testEntityManager.persist(fourthChain));
			testEntityManager.flush();
			testEntityManager.clear();
			logger.info("Test data was created successfully: instances of '{}' were added to the database.", Chain.class);
		} catch(Exception exception) {
			logger.error("Exception has occurred during the creation of test data ('{}' instances): {}.", Chain.class, exception);
		}
	}
	
	private void removeListOfChains() {
		logger.info("Removing of test data.");
		
		try {
			activeChains.stream().forEach((Chain chain) ->	testEntityManager.remove(testEntityManager.merge(chain)));	
			inactiveChains.stream().forEach((Chain chain) ->	testEntityManager.remove(testEntityManager.merge(chain)));	
			testEntityManager.flush();
			logger.info("Test data was cleaned successfully: instances of '{}' were removed from the database.", Chain.class);
		} catch (Exception exception) {
			logger.error("Exception has occurred during the cleaning of test data ('{}' instances): {}.", Chain.class, exception);
		}
	}
	
	@Test
	public void test_get_all_chains() throws Exception {
		createListOfChains();
		List<ChainDTO> expectedChainDTOs = activeChains
				.stream()
				.map(ChainDTO::new)
				.collect(Collectors.toList());
		String expectedJson = objectMapper.writeValueAsString(expectedChainDTOs);
		logger.info("Starting of request '{}({})' execution.", "GET", "/chains");
		stopWatch.start();
		MvcResult mvcResult = this.mockMvc.perform(get("/chains")
								  .accept(ApiConfigConstants.API_V_2_0))
								  .andExpect(status().isOk())
								  .andExpect(content().encoding(StandardCharsets.UTF_8.name()))
								  .andExpect(content().contentType("application/vnd.naakcii.api-v2.0+json;charset=UTF-8"))
								  .andDo(print())
								  .andReturn();
		stopWatch.stop();
		logger.info("Execution of request '{}({})' has finished.", "GET", "/chains");
		logger.info("Execution time is: {} milliseconds.", stopWatch.getTotalTimeMillis());
		String resultJson = mvcResult.getResponse().getContentAsString();
		assertEquals("Expected JSON should be: ["
				+ "{id:1, name:'Алми', logo:'almi.png', link:'www.almi.by'},"
				+ "{id:2, name:'Виталюр', logo:'vitalur.png', link:'www.vitalur.by'},"
				+ "{id:3, name:'Евроопт', logo:'evroopt.png', link:'www.evroopt.by'}"
				+ "].",	expectedJson, resultJson);
		removeListOfChains();
	}
	
	@After
	public void tearDown() {
		objectMapper = null;
		stopWatch = null;
		activeChains = null;
		inactiveChains = null;
	}
}
