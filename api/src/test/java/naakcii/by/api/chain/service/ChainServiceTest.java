import naakcii.by.api.action.service.ActionService;
import naakcii.by.api.chain.repository.ChainRepository;
import naakcii.by.api.chain.repository.model.Chain;
import naakcii.by.api.chain.service.ChainService;
import naakcii.by.api.chain.service.modelDTO.ChainDTO;
import naakcii.by.api.chain.service.serviceImpl.ChainServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;

@DirtiesContext
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ChainServiceTest.ChainServiceTestContextConfiguration.class)
public class ChainServiceTest {

    @TestConfiguration
    @ComponentScan(basePackages = "naakcii.by.api.chain.service")
    static class ChainServiceTestContextConfiguration {
        @Bean
        public ChainService chainService() {
            return new ChainServiceImpl() {
            };
        }
    }

    @Autowired
    ChainService chainService;
    @MockBean
    ChainRepository repository;
    @MockBean
    ActionService actionService;


    public List<Chain> setUp() {
        List<Chain> chains = new ArrayList<>();
        Chain chain = new Chain();
        chain.setId(1L);
        chain.setName("chain_1");
        Chain chain2 = new Chain();
        chain2.setId(2L);
        chain2.setName("chain_2");
        Chain chain3 = new Chain();
        chain3.setId(3L);
        chain3.setName("chain_3");
        chains.add(chain);
        chains.add(chain2);
        chains.add(chain3);
        return chains;
    }


    @Test
    public void fullTest() {
        List<Chain> chains = setUp();
        Mockito.when(repository.findAllByOrderByNameAsc()).thenReturn(chains);
        Assert.assertThat(chains.size(), is(3));

        List<ChainDTO> chainDTOList = chainService.findAll();
        Assert.assertThat(chainDTOList.size(), is(3));

        ChainDTO chain1 = chainDTOList.get(1);
        ChainDTO chain2 = chainDTOList.get(2);

        Assert.assertThat(chain1.getId(), is(2L));
        Assert.assertThat(chain1.getName(), is("chain_2"));

        Assert.assertThat(chain2.getId(), is(3L));
        Assert.assertThat(chain2.getName(), is("chain_3"));
    }

}
