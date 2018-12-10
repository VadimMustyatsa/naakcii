package naakcii.by.api.subscriber.service;

import naakcii.by.api.subscriber.repository.SubscriberRepository;
import naakcii.by.api.subscriber.repository.model.Subscriber;
import naakcii.by.api.subscriber.service.modelDTO.SubscriberDTO;
import naakcii.by.api.subscriber.service.serviceImpl.SubscriberServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.is;

@DirtiesContext
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = SubscriberServiceImplTest.SubscriberTestConfig.class)
public class SubscriberServiceImplTest {

    @MockBean
    private SubscriberRepository subscriberRepository;

    @Autowired
    private SubscriberService subscriberService;

    @Test
    public void saveTest() throws Exception {
        String email = "email@email.com";
        Subscriber subscriber = new Subscriber();
        subscriber.setEmail(email);
        Subscriber subscriberFromDb = new Subscriber();
        subscriberFromDb.setId(1L);
        subscriberFromDb.setEmail(email);
        Mockito.when(subscriberRepository.findByEmail(email)).thenReturn(null);
        Mockito.when(subscriberRepository.save(subscriber)).thenReturn(subscriberFromDb);
        SubscriberDTO subscriberDTO = subscriberService.save(email);
        Assert.assertThat(subscriberDTO.getEmail(), is(email));
        Assert.assertThat(subscriberDTO.getId(), is(1L));
    }

    @Configuration
    @ComponentScan(basePackages = "naakcii.by.api.subscriber.service")
    static class SubscriberTestConfig {

        @Bean
        public SubscriberService subscriberService() {
            return new SubscriberServiceImpl();
        }
    }
}
