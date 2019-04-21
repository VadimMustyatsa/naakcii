package naakcii.by.api.util.tasks;

import naakcii.by.api.chain.ChainRepository;
import naakcii.by.api.util.parser.DataParser;
import naakcii.by.api.util.parser.ParsingResult;
import naakcii.by.api.util.slack.SlackNotification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DataParserTask {

    private static final Logger logger = LogManager.getLogger(DataParserTask.class);
    private SlackNotification slackNotification;
    private ChainRepository chainRepository;
    private DataParser dataParser;
    private Environment env;

    @Autowired
    public DataParserTask(SlackNotification slackNotification, ChainRepository chainRepositor,
                          DataParser dataParser, Environment env) {
        this.slackNotification = slackNotification;
        this.chainRepository = chainRepositor;
        this.dataParser = dataParser;
        this.env = env;
    }

    @Scheduled(cron = "0 15 3 * * *")
    public void parsingTask() {
        logger.info("Parsing start:");
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat date = new SimpleDateFormat("yyyy_MM_dd");
        StringBuilder fileHomePath = getHomePath();
        slackNotification.sendMessageToNotificationsChannel("_*Parsing start:*_");
        List<String> synonyms = chainRepository.getAllSynonyms();
        slackNotification.sendMessageToNotificationsChannel("Chain list: _" + synonyms + "_");
        for (String chainSynonym : synonyms) {
            try {
                logger.info("Scan folder: " + chainSynonym);
                slackNotification.sendMessageToNotificationsChannel("*Scan folder:* _" + chainSynonym + "_");
                StringBuilder currentChainFolderPath = new StringBuilder(fileHomePath);
                currentChainFolderPath.append(chainSynonym)
                        .append(File.separator)
                        .append(env.getProperty("ftp.action.data.folder.name"))
                        .append(File.separator);
                File folder = new File(currentChainFolderPath.toString());
                File[] folderFiles = folder.listFiles();
                if (folderFiles != null) {
                    for (File file : folderFiles) {
                        Pattern pattern = Pattern.compile(".*" + date.format(calendar.getTime()) + "\\.xlsx");
                        Matcher matcher = pattern.matcher(file.getName());
                        if (matcher.find()) {
                            logger.info("Next file was parsed: " + file.getName());
                            slackNotification.sendMessageToNotificationsChannel("Next file was parsed: `" + file.getName() + "`");
                            List<ParsingResult<?, ?>> parsingResults = dataParser.parseChainProducts(file.getPath(), chainSynonym);
                            parsingResults.forEach((ParsingResult<?, ?> result) ->
                                    slackNotification.sendCodeSnippetToNotificationsChannel(result.toString()));
                        } else {
                            logger.info("Next file was skipped: " + file.getName());
                            slackNotification.sendMessageToNotificationsChannel("Next file was skipped: " + file.getName());
                        }
                    }
                }
            } catch (Exception e) {
                slackNotification.sendMessageToNotificationsChannel("Parsing task terminated: " + e.getMessage());
                logger.info(e.getMessage());
            }
        }
    }

    private StringBuilder getHomePath() {
        StringBuilder homePath = new StringBuilder();
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().startsWith("windows")) {
            homePath.append(env.getProperty("ftp.action.data.windows.home.path"))
                    .append(File.separator);
        } else if (osName.toLowerCase().startsWith("linux")) {
            homePath.append(env.getProperty("ftp.action.data.linux.home.path"))
                    .append(File.separator);
        }
        return homePath;
    }
}
