package naakcii.by.api.subscriber.service.util;

import naakcii.by.api.subscriber.repository.model.Subscriber;
import naakcii.by.api.subscriber.service.modelDTO.SubscriberDTO;
import org.springframework.stereotype.Component;

@Component
public class SubscriberConverter {

    public Subscriber convertFromDto(SubscriberDTO subscriberDTO) {
        Subscriber subscriber = new Subscriber();
        subscriber.setId(subscriberDTO.getId());
        subscriber.setEmail(subscriberDTO.getEmail());
        return subscriber;
    }

    public SubscriberDTO convertToDto(Subscriber subscriber) {
        SubscriberDTO subscriberDTO = new SubscriberDTO();
        subscriberDTO.setId(subscriber.getId());
        subscriberDTO.setEmail(subscriber.getEmail());
        return subscriberDTO;
    }
}
