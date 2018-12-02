package naakcii.by.api.chain;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ChainControllerTest {

	private ChainController chainController;
	private List<ChainDTO> chainDTOs;
	
	@Mock
	private
	ChainService chainService;
	
	@Before
	public void setUp() {
		chainController = new ChainController(chainService);
	}
	
	@Test
	public void test_get_all_chains() {
		when(chainService.getAllChains()).thenReturn(chainDTOs);
		chainController.getAllChains();
		verify(chainService).getAllChains();
	}
}
