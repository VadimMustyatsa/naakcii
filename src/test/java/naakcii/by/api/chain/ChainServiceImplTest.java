package naakcii.by.api.chain;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import naakcii.by.api.util.ObjectFactory;

@RunWith(MockitoJUnitRunner.class)
public class ChainServiceImplTest {

	private ChainService chainService;
	
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
		chainService = new ChainServiceImpl(chainRepository, objectFactory);
	}
	
	private List<Chain> createListOfChains() {
		List<Chain> chains= new ArrayList<>();
		chains.add(firstChain);
		chains.add(null);
		chains.add(secondChain);
		chains.add(null);
		chains.add(thirdChain);
		return chains;
	}
	
	@Test
	public void test_get_all_chains() {
		List<ChainDTO> expectedChainDTOs = new ArrayList<>();
		expectedChainDTOs.add(firstChainDTO);
		expectedChainDTOs.add(secondChainDTO);
		expectedChainDTOs.add(thirdChainDTO);
		when(chainRepository.findAllByIsActiveTrueOrderByNameAsc()).thenReturn(createListOfChains());
		when(objectFactory.getInstance(ChainDTO.class, firstChain)).thenReturn(firstChainDTO);
		when(objectFactory.getInstance(ChainDTO.class, secondChain)).thenReturn(secondChainDTO);
		when(objectFactory.getInstance(ChainDTO.class, thirdChain)).thenReturn(thirdChainDTO);
		List<ChainDTO> resultChainDTOs = chainService.getAllChains();
		verify(chainRepository).findAllByIsActiveTrueOrderByNameAsc();
		verify(objectFactory).getInstance(ChainDTO.class, firstChain);
		verify(objectFactory).getInstance(ChainDTO.class, secondChain);
		verify(objectFactory).getInstance(ChainDTO.class, thirdChain);
		assertEquals("Size of the result list of chain data transfer objects should be 3.", resultChainDTOs.size(), 3);
		assertEquals("Result list of chain data transfer objects should be: [firstChainDTO, secondChainDTO, thirdChainDTO].", expectedChainDTOs, resultChainDTOs);
	}
}
