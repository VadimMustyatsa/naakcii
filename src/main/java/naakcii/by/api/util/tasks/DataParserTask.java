package naakcii.by.api.util.tasks;

import naakcii.by.api.chain.ChainRepository;
import naakcii.by.api.util.parser.DataParser;
import naakcii.by.api.util.parser.multisheet.ParsingResult;
import naakcii.by.api.util.slack.SlackNotification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
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

    @Value("${ftp.action.data.path}")
    private String dataPath;

    @Value("${ftp.action.data.folder.name}")
    private String dataFolder;

    @Value("${ftp.action.log.path}")
    private String logFolder;

    @Autowired
    public DataParserTask(SlackNotification slackNotification, ChainRepository chainRepository,
                          DataParser dataParser) {
        this.slackNotification = slackNotification;
        this.chainRepository = chainRepository;
        this.dataParser = dataParser;
    }

    @Scheduled(cron = "0 0/30 * * * *")
    public void parsingTask() {
        logger.info("Parsing start:");
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat date = new SimpleDateFormat("yyyy_MM_dd");
        StringBuilder fileHomePath = new StringBuilder(dataPath);
        slackNotification.sendMessageToNotificationsChannel("_*Parsing start:*_");
        List<String> synonyms = chainRepository.getAllSynonyms();
        slackNotification.sendMessageToNotificationsChannel("_ List of chains: _" + synonyms + "_");
        for (String chainSynonym : synonyms) {
            try {
                StringBuilder currentChainFolderPath = new StringBuilder(fileHomePath);
                currentChainFolderPath.append(chainSynonym)
                        .append(File.separator)
                        .append(dataFolder)
                        .append(File.separator);
                File folder = new File(currentChainFolderPath.toString());
                logger.info("Scan folder: " + chainSynonym);
                slackNotification.sendMessageToNotificationsChannel("*Scan folder:* _" + chainSynonym + "_ `(" + folder + ")`");
                File[] folderFiles = folder.listFiles();
                if (folderFiles != null) {
                    for (File file : folderFiles) {
                        Pattern pattern = Pattern.compile(".*" + date.format(calendar.getTime()) + "\\.xlsx");
                        Matcher matcher = pattern.matcher(file.getName());
                        if (matcher.find()) {
                            logger.info("Next file was parsed: " + file.getName());
                            slackNotification.sendMessageToNotificationsChannel("Next file was parsed: `" + file.getName() + "`");
                            List<ParsingResult<?>> parsingResults = dataParser.parseChainProducts(file.getPath(), chainSynonym);
                            parsingResults.forEach((ParsingResult<?> result) ->
                                    saveAndNotifyParsingResult(result.toString(), file.getName()));
                        } else {
                            logger.info("Next file was skipped: " + file.getName());
                        }
                    }
                }
            } catch (Exception e) {
                slackNotification.sendMessageToNotificationsChannel("Parsing task was terminated: " + e.getMessage());
                logger.info(e.getMessage());
            }
        }
    }

    private void saveAndNotifyParsingResult(String result, String filename) {
        String logFileName = filename.replaceAll("\\.", "_");
        File file = new File(logFolder + logFileName + ".log");
        try (
                Writer writer = new FileWriter(file)
        ) {
            writer.write(result);
            slackNotification.sendCodeSnippetToNotificationsChannel(file);
        } catch (Exception e) {
            logger.error(e.getMessage());
            slackNotification.sendMessageToNotificationsChannel(e.getMessage());
        }

    }
}
