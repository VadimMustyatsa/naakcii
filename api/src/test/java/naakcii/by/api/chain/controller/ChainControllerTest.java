package naakcii.by.api.chain.controller;

import naakcii.by.api.chain.service.ChainService;
import naakcii.by.api.chain.service.modelDTO.ChainDTO;
import naakcii.by.api.config.ApiConfigConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ChainController.class)
public class ChainControllerTest {

    @MockBean
    private ChainService chainService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void givenChainDtoThenReturnJsonArray() throws Exception {
        ChainDTO firstChainDto = new ChainDTO();
        ChainDTO secondChainDto = new ChainDTO();
        firstChainDto.setName("firstChainDto");
        secondChainDto.setName("secondChainDto");
        given(chainService.findAll()).willReturn(Arrays.asList(firstChainDto, secondChainDto));
        mockMvc.perform(get("/chain")
                .accept(ApiConfigConstants.API_V1_0))
                .andExpect(status().isOk())
                .andExpect(content().encoding(StandardCharsets.UTF_8.name()))
                .andExpect(content().contentTypeCompatibleWith(ApiConfigConstants.API_V1_0))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(firstChainDto.getName())))
                .andExpect(jsonPath("$[1].name", is(secondChainDto.getName())));
    }
}
