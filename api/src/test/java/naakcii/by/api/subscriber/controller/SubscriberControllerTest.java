package naakcii.by.api.subscriber.controller;

import naakcii.by.api.config.ApiConfigConstants;
import naakcii.by.api.subscriber.service.SubscriberService;
import naakcii.by.api.subscriber.service.modelDTO.SubscriberDTO;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(SubscriberController.class)
public class SubscriberControllerTest {

    @MockBean
    private SubscriberService subscriberService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void givenSubscriberDtoThenReturnJson() throws Exception {
        String jsonString = "{\"email\" : \"email@email.com\"}";
        String email = "email@email.com";
        SubscriberDTO subscriberDTO = new SubscriberDTO();
        subscriberDTO.setId(1L);
        subscriberDTO.setEmail(email);
        given(subscriberService.save(email)).willReturn(subscriberDTO);
        mockMvc.perform(post("/subscribers/add")
                .content(jsonString)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(ApiConfigConstants.API_V1_0))
                .andExpect(status().isOk())
                .andExpect(content().encoding(StandardCharsets.UTF_8.name()))
                .andExpect(content().contentTypeCompatibleWith(ApiConfigConstants.API_V1_0))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.email", is(email)));
    }

    @Test
    public void emptyBody_ShouldReturnHttpStatusCode400() throws Exception {
        mockMvc.perform(post("/subscribers/add")
                .accept(ApiConfigConstants.API_V1_0))
                .andExpect(status().is4xxClientError());
    }
}
