package naakcii.by.api.util.slack;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SlackNotification {

    @Value("${slack.channel.notifications.webhook.url}")
    private String notificationsChannelWebHookUrl;

    public void sendMessageToNotificationsChannel(String message) {
        sendToSlackBackEnd(message, notificationsChannelWebHookUrl);
    }

    private void sendToSlackBackEnd(String message, String url) {
        message = message.replaceAll("\\\\", "\\\\\\\\");
        String body = String.format("{\"text\":\"%s\"}", message);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> entity = new HttpEntity<String>(body, headers);
        restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }
}
