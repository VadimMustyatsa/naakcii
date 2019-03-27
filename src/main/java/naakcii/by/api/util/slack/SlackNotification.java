package naakcii.by.api.util.slack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.net.URI;

@Component
public class SlackNotification {

    private static final Logger logger = LogManager.getLogger(SlackNotification.class);
    private static final String SLACK_FILE_UPLOAD_URL = "https://slack.com/api/files.upload";
    private static final String SLACK_CHAT_POST_MESSAGE_URL = "https://slack.com/api/chat.postMessage";

    @Value("${slack.bot.oauth.token}")
    private String botToken;

    @Value("${slack.notification.channel}")
    private String notificationChannel;

    public void sendMessageToNotificationsChannel(String message) {
        try {
            URI uri = UriComponentsBuilder.fromHttpUrl(SLACK_CHAT_POST_MESSAGE_URL)
                    .queryParam("token", botToken)
                    .queryParam("channel", notificationChannel)
                    .queryParam("text", message)
                    .build()
                    .toUri();
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> exchange = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
            logger.info(exchange.toString());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void sendCodeSnippetToNotificationsChannel(File snippet) {
        try {
            URI uri = UriComponentsBuilder.fromHttpUrl(SLACK_FILE_UPLOAD_URL).build().toUri();
            RestTemplate restTemplate = new RestTemplate();
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new FileSystemResource(snippet));
            body.add("token", botToken);
            body.add("channels", notificationChannel);
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);
            ResponseEntity<String> exchange = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
            logger.info(exchange.toString());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
