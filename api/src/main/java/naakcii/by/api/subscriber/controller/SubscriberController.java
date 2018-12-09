package naakcii.by.api.subscriber.controller;

import naakcii.by.api.subscriber.service.SubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/subscribers"})
public class SubscriberController {

    @Autowired
    private SubscriberService subscriberService;

    @PostMapping({"/add"})
    public void subscribe(@RequestParam(value = "email", required = true) String email) {
        subscriberService.save(email);
    }

}
