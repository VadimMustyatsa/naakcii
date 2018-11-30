package naakcii.by.api.viber.service.serviceImpl;

import naakcii.by.api.viber.service.ViberService;
import naakcii.by.api.viber.service.modelDTO.ViberMessageDTO;
import naakcii.by.api.viber.service.modelDTO.ViberSenderDTO;
import naakcii.by.api.viber.service.modelDTO.ViberType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;

@Service
public class ViberServiceImpl implements ViberService {

    private static final Logger logger = LogManager.getLogger(ViberServiceImpl.class);

    @Value("${viber.file.path}")
    private String viberFilePath;

    @Value("${viber.message.welcome}")
    private String welcomeMessageText;

    @Value("${viber.min.api.version}")
    private Integer apiVersion;

    @Autowired
    private ViberSenderDTO sender;

    @Override
    public ViberMessageDTO getFileMessage(String userId, String fileName) {
        ViberMessageDTO fileMessage = new ViberMessageDTO(apiVersion);
        fileMessage.setSender(sender);
        fileMessage.setReceiver(userId);
        fileMessage.setType(ViberType.FILE);
        String fileFullPath = MessageFormat.format(viberFilePath, fileName);
        fileMessage.setFileUrl(fileFullPath);
        fileMessage.setFileName(fileName);
        fileMessage.setSize(getFileSize(fileFullPath));

        return fileMessage;
    }

    private Integer getFileSize(String fileUrl) {
        URLConnection conn = null;
        try {
            URL url = new URL(fileUrl);
            conn = url.openConnection();
            if (conn instanceof HttpsURLConnection) {
                ((HttpsURLConnection) conn).setRequestMethod("HEAD");
                conn.connect();
            }
            return conn.getContentLength();
        } catch (IOException e) {
            logger.error("File: " + fileUrl);
            throw new RuntimeException(e);
        } finally {
            if (conn instanceof HttpURLConnection) {
                ((HttpsURLConnection) conn).disconnect();
            }
        }
    }

    @Override
    public ViberMessageDTO getWelcomeMessage() {
        ViberMessageDTO welcomeMessage = new ViberMessageDTO(apiVersion);
        welcomeMessage.setSender(sender);
        welcomeMessage.setType(ViberType.TEXT);
        welcomeMessage.setText(welcomeMessageText);
        return welcomeMessage;
    }

    @Override
    public ViberMessageDTO getTextMessage(String userId, String text) {
        ViberMessageDTO textMessage = new ViberMessageDTO(apiVersion);
        textMessage.setSender(sender);
        textMessage.setReceiver(userId);
        textMessage.setType(ViberType.TEXT);
        textMessage.setText(text);
        return textMessage;
    }
}
