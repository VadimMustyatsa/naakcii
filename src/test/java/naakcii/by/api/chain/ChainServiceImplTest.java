package naakcii.by.api.chain;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import naakcii.by.api.util.ObjectFactory;

@RunWith(MockitoJUnitRunner.class)
public class ChainServiceImplTest {

	private ChainServiceImpl chainServiceImpl;
	private List<Chain> chains;
	
	@Mock
	private ChainRepository chainRepository;
	
	@Mock
	private ObjectFactory objectFactory;
	
	@Mock
	private Chain firstChain;
	
	@Mock
	private Chain secondChain;
	
	@Mock
	private Chain thirdChain;
	
	@Mock
	private ChainDTO firstChainDTO;
	
	@Mock
	private ChainDTO secondChainDTO;
	
	@Mock
	private ChainDTO thirdChainDTO;
	
	@Before
	public void setUp() {
		chainServiceImpl = new ChainServiceImpl(chainRepository, objectFactory);
	}
	
	private void createListOfChains() {
		chains = new ArrayList<>();
		chains.add(firstChain);
		chains.add(null);
		chains.add(secondChain);
		chains.add(null);
		chains.add(thirdChain);
	}
	
	@Test
	public void test_get_all_chains() {
		createListOfChains();
		List<ChainDTO> expectedChainDTOs = new ArrayList<>();
		expectedChainDTOs.add(firstChainDTO);
		expectedChainDTOs.add(secondChainDTO);
		expectedChainDTOs.add(thirdChainDTO);
		when(chainRepository.findAllByIsActiveTrueOrderByNameAsc()).thenReturn(chains);
		when(objectFactory.getInstance(ChainDTO.class, firstChain)).thenReturn(firstChainDTO);
		when(objectFactory.getInstance(ChainDTO.class, secondChain)).thenReturn(secondChainDTO);
		when(objectFactory.getInstance(ChainDTO.class, thirdChain)).thenReturn(thirdChainDTO);
		List<ChainDTO> resultChainDTOs = chainServiceImpl.getAllChains();
		verify(chainRepository).findAllByIsActiveTrueOrderByNameAsc();
		verify(objectFactory).getInstance(ChainDTO.class, firstChain);
		verify(objectFactory).getInstance(ChainDTO.class, secondChain);
		verify(objectFactory).getInstance(ChainDTO.class, thirdChain);
		assertEquals("Size of the result list of chain data transfer objects should be 3.", resultChainDTOs.size(), 3);
		assertEquals("Result list of chain data transfer objects should be [firstChainDTO, secondChainDTO, thirdChainDTO]", expectedChainDTOs, resultChainDTOs);
	}
	
	@After
	public void tearDown() {
		chainServiceImpl = null;
		chains = null;
	}
}
