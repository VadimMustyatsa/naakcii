package naakcii.by.api.subscriber;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import naakcii.by.api.config.ApiConfigConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "REST API для сущности Subscriber")
@RestController
@RequestMapping({"/subscribers"})
public class SubscriberController {

    @Autowired
    private SubscriberService subscriberService;

    @PostMapping(produces = ApiConfigConstants.API_V_2_0)
    @ApiOperation("Создает нового подписчика сервиса")
    public SubscriberDTO subscribe(@RequestBody SubscriberDTO subscriberDTO) {
        return subscriberService.save(subscriberDTO.getEmail());
    }
}
