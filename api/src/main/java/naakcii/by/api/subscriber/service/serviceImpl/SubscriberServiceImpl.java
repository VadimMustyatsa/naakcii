package naakcii.by.api.subscriber.service.serviceImpl;

import naakcii.by.api.subscriber.repository.SubscriberRepository;
import naakcii.by.api.subscriber.repository.model.Subscriber;
import naakcii.by.api.subscriber.service.SubscriberService;
import naakcii.by.api.subscriber.service.modelDTO.SubscriberDTO;
import naakcii.by.api.subscriber.service.util.SubscriberConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriberServiceImpl implements SubscriberService {

    @Autowired
    private SubscriberRepository subscriberRepository;

    @Autowired
    private SubscriberConverter subscriberConverter;

    @Override
    public void save(String email) {
        Subscriber subscriberByEmail = subscriberRepository.findByEmail(email);
        if (subscriberByEmail == null) {
            SubscriberDTO subscriberDTO = new SubscriberDTO(email);
            Subscriber subscriber = subscriberConverter.convertFromDto(subscriberDTO);
            subscriberRepository.save(subscriber);
        }
    }
}
