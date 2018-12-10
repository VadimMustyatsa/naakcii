package naakcii.by.api.subscriber.service;

import naakcii.by.api.subscriber.service.modelDTO.SubscriberDTO;

public interface SubscriberService {
    SubscriberDTO save(String email);
}
