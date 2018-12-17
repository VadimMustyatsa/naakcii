package naakcii.by.api.subscriber.controller;

import naakcii.by.api.subscriber.service.SubscriberService;
import naakcii.by.api.subscriber.service.modelDTO.SubscriberDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/subscribers"})
public class SubscriberController {

    @Autowired
    private SubscriberService subscriberService;

    @PostMapping
    public SubscriberDTO subscribe(@RequestBody SubscriberDTO subscriberDTO) {
        return subscriberService.save(subscriberDTO.getEmail());
    }
}
