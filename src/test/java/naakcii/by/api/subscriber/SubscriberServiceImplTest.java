package naakcii.by.api.subscriber;

import naakcii.by.api.util.ObjectFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SubscriberServiceImplTest {

    @Mock
    private SubscriberRepository subscriberRepository;

    @Mock
    private ObjectFactory objectFactory;

    @Autowired
    private SubscriberService subscriberService;

    @Before
    public void setUp() {
        subscriberService = new SubscriberServiceImpl(subscriberRepository, objectFactory);
    }

    @Test
    public void test_save() {
        String email = "email@email.com";
        Subscriber subscriber = new Subscriber(email);
        Subscriber subscriberFromDb = new Subscriber(email);
        subscriberFromDb.setId(1L);
        SubscriberDTO subscriberDtoFromDb = new SubscriberDTO(subscriberFromDb);
        when(subscriberRepository.findByEmail(email)).thenReturn(null);
        when(subscriberRepository.save(subscriber)).thenReturn(subscriberFromDb);
        when(objectFactory.getInstance(SubscriberDTO.class, subscriber)).thenReturn(subscriberDtoFromDb);
        SubscriberDTO subscriberDTO = subscriberService.save(email);
        Assert.assertThat(subscriberDTO.getEmail(), is(email));
        Assert.assertThat(subscriberDTO.getId(), is(1L));
    }

}
