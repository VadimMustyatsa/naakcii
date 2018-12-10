package naakcii.by.api.subscriber.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import naakcii.by.api.config.ApiConfigConstants;
import naakcii.by.api.subscriber.service.SubscriberService;
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

    @PostMapping(value = {"/add"}, produces = ApiConfigConstants.API_V1_0)
    public void subscribe(@RequestBody ObjectNode json) {
        String email = json.get("email").textValue();
        subscriberService.save(email);
    }
}
