package naakcii.by.api.viber.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import naakcii.by.api.viber.service.ViberService;
import naakcii.by.api.viber.service.modelDTO.ViberEventDTO;
import naakcii.by.api.viber.service.modelDTO.ViberMessageDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping({"/viber"})
public class ViberController {

    private static final Logger logger = LogManager.getLogger(ViberController.class);

    @Value("${viber.token}")
    private String viberToken;

    @Value("${viber.token.name}")
    private String viberTokenName;

    @Value("${viber.message.send.url}")
    private String viberMessageUrl;

    @Autowired
    private ViberService service;

    @PostMapping
    public ViberMessageDTO viberWebhook(@RequestBody ObjectNode json, HttpServletResponse response) {
        response.addHeader(viberTokenName, viberToken);
        ViberEventDTO event = ViberEventDTO.valueOf(json.get("event").textValue().toUpperCase());
        String messageToken = json.get("message_token").toString();
        String userId;
        ViberMessageDTO message = new ViberMessageDTO();
        switch (event) {
            case CONVERSATION_STARTED:
                userId = json.findValue("user").get("id").textValue();
                Boolean isSubscribed = json.get("subscribed").asBoolean();
                logger.info("Event: " + event
                        + " user: " + userId
                        + " subscribed: " + isSubscribed
                        + " message_token: " + messageToken);
                if (isSubscribed) {
                    String fileNameFromContext = json.get("context").textValue();
                    message = service.getFileMessage(userId, fileNameFromContext);
                    sendMessageToViberService(message);
                } else {
                    message = service.getWelcomeMessage();
                }
                break;
            case MESSAGE:
                userId = json.findValue("sender").get("id").textValue();
                String fileName = json.findValue("message").get("text").textValue();
                message = service.getFileMessage(userId, fileName);
                logger.info("Event: " + event + " user: " + userId + " message_token: " + messageToken);
                sendMessageToViberService(message);
                break;
            default:
                logger.info("Wrong event: " + event + " message_token: " + messageToken);
        }
        return message;
    }

    private void sendMessageToViberService(ViberMessageDTO message) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.add(viberTokenName, viberToken);
        HttpEntity<ViberMessageDTO> entity = new HttpEntity<ViberMessageDTO>(message, headers);
        ResponseEntity<String> response = restTemplate
                .exchange(viberMessageUrl, HttpMethod.POST, entity, String.class);
        logger.info(response.toString());
    }

}
