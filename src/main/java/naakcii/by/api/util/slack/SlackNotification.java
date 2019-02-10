package naakcii.by.api.util.slack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
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
    private static final String NOTIFICATION_WEBHOOK = "https://hooks.slack.com/services/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";
    private static final String BOT_TOKEN = "xoxb-311663550839-528317188997-WbtXTzeafDhZynpilj3LEFZu";
    private static final String SNIPPET_URL = "https://slack.com/api/files.upload";
    private static final String SNIPPET_CHANNEL = "notifications";

    public void sendMessageToNotificationsChannel(String message) {
        sendMessageToSlackWebHook(message, NOTIFICATION_WEBHOOK);
    }

    private void sendMessageToSlackWebHook(String message, String webHook) {
        try {
            String body = String.format("{\"text\":\"%s\"}", message);
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            HttpEntity<String> entity = new HttpEntity<String>(body, headers);
            restTemplate.exchange(webHook, HttpMethod.POST, entity, String.class);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void sendCodeSnippetToNotificationsChannel(File snippet) {
        try {
            URI uri = UriComponentsBuilder.fromHttpUrl(SNIPPET_URL)
                    .queryParam("token", BOT_TOKEN)
                    .queryParam("channels", SNIPPET_CHANNEL)
                    .build()
                    .toUri();
            RestTemplate restTemplate = new RestTemplate();
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new FileSystemResource(snippet));
            HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, new HttpHeaders());
            restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
