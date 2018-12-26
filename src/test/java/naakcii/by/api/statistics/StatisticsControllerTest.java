package naakcii.by.api.statistics;

import naakcii.by.api.config.ApiConfigConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(StatisticsController.class)
public class StatisticsControllerTest {

    @MockBean
    private StatisticsService statisticsService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void test_return_statistics_json() throws Exception {
        StatisticsDTO statisticsDTO = new StatisticsDTO();
        statisticsDTO.setChainQuantity(1);
        statisticsDTO.setAverageDiscountPercentage(11);
        statisticsDTO.setDiscountedProducts(111);
        statisticsDTO.setCreationDateMillis(100000L);
        given(statisticsService.findCurrentStatistics()).willReturn(statisticsDTO);
        mockMvc.perform(get("/statistics")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(ApiConfigConstants.API_V_2_0))
                .andExpect(status().isOk())
                .andExpect(content().encoding(StandardCharsets.UTF_8.name()))
                .andExpect(content().contentTypeCompatibleWith(ApiConfigConstants.API_V_2_0))
                .andExpect(jsonPath("$.chainQuantity", is(1)))
                .andExpect(jsonPath("$.discountedProducts", is(111)))
                .andExpect(jsonPath("$.averageDiscountPercentage", is(11)))
                .andExpect(jsonPath("$.creationDateMillis", is(100000)));
    }
}
