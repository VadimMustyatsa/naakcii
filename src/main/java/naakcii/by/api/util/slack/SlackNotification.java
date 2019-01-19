package naakcii.by.api.util.slack;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;

@Component
public class SlackNotification {

    private static final String NOTIFICATION_WEBHOOK = "https://hooks.slack.com/services/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";
    private static final String BOT_TOKEN = "xoxb-311663550839-528317188997-NAES1sIGXBEckUt7eGSnABKd";
    private static final String SNIPPET_URL = "https://slack.com/api/files.upload";
    private static final String SNIPPET_CHANNEL = "notifications";
    private static final String SNIPPET_FILE_NAME = "Parsing_result.txt";
    private static final String SNIPPET_TYPE = "text";

    public void sendMessageToNotificationsChannel(String message) {
        sendMessageToSlackWebHook(message, NOTIFICATION_WEBHOOK);
    }

    private void sendMessageToSlackWebHook(String message, String webHook) {
        String body = String.format("{\"text\":\"%s\"}", message);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> entity = new HttpEntity<String>(body, headers);
        restTemplate.exchange(webHook, HttpMethod.POST, entity, String.class);
    }

    public void sendCodeSnippetToNotificationsChannel(String snippet) {
        URI uri = UriComponentsBuilder.fromHttpUrl(SNIPPET_URL)
                .queryParam("token", BOT_TOKEN)
                .queryParam("channels", SNIPPET_CHANNEL)
                .queryParam("content", snippet)
                .queryParam("filename", SNIPPET_FILE_NAME)
                .queryParam("filetype", SNIPPET_TYPE)
                .build()
                .toUri();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("charset", StandardCharsets.UTF_8.name());
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
    }
}
