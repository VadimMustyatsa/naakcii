package naakcii.by.api.subscriber;

import naakcii.by.api.config.ApiConfigConstants;
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

    @PostMapping(produces = ApiConfigConstants.API_V_2_0)
    public SubscriberDTO subscribe(@RequestBody SubscriberDTO subscriberDTO) {
        return subscriberService.save(subscriberDTO.getEmail());
    }
}
