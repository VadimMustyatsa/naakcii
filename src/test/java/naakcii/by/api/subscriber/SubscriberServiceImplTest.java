package naakcii.by.api.subscriber;

import naakcii.by.api.util.ObjectFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
public class SubscriberServiceImplTest {

    @MockBean
    private SubscriberRepository subscriberRepository;

    @Autowired
    private SubscriberService subscriberService;

    @Test
    public void test_save() throws Exception {
        String email = "email@email.com";
        Subscriber subscriber = new Subscriber(email);
        Subscriber subscriberFromDb = new Subscriber(email);
        subscriberFromDb.setId(1L);
        SubscriberDTO subscriberDtoFromDb = new SubscriberDTO(subscriberFromDb);
        Mockito.when(subscriberRepository.findByEmail(email)).thenReturn(null);
        Mockito.when(subscriberRepository.save(subscriber)).thenReturn(subscriberFromDb);
        ObjectFactory mock = Mockito.mock(ObjectFactory.class);
        Mockito.when(mock.getInstance(SubscriberDTO.class, subscriber)).thenReturn(subscriberDtoFromDb);
        SubscriberDTO subscriberDTO = subscriberService.save(email);
        Assert.assertThat(subscriberDTO.getEmail(), is(email));
        Assert.assertThat(subscriberDTO.getId(), is(1L));
    }

    @Configuration
    @ComponentScan(basePackages = {"naakcii.by.api.subscriber", "naakcii.by.api.util"})
    static class SubscriberTestConfig {

        @Bean
        public SubscriberService subscriberService() {
            return new SubscriberServiceImpl();
        }
    }
}
