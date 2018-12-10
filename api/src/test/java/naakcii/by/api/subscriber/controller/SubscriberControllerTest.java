package naakcii.by.api.subscriber.controller;

import naakcii.by.api.config.ApiConfigConstants;
import naakcii.by.api.subscriber.service.SubscriberService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(SubscriberController.class)
public class SubscriberControllerTest {

    @MockBean
    private SubscriberService subscriberService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void expectStatusOk() throws Exception {
        String jsonString = "{\"email\" : \"email@email.com\"}";
        mockMvc.perform(post("/subscribers/add").content(jsonString)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(ApiConfigConstants.API_V1_0))
                .andExpect(status().isOk());
    }
}
