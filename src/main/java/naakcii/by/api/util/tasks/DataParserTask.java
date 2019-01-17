package naakcii.by.api.util.tasks;

import naakcii.by.api.chain.ChainRepository;
import naakcii.by.api.util.parser.IDataParser;
import naakcii.by.api.util.parser.multisheet.ParsingResult;
import naakcii.by.api.util.slack.SlackNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Autowired
    private SlackNotification slackNotification;

    @Autowired
    private ChainRepository chainRepository;

    @Autowired
    private IDataParser dataParser;

    @Value("${ftp.action.data.folder.name}")
    private String actionDataFolder;

    @Value("${ftp.action.data.linux.home.path}")
    private String linuxPath;

    @Value("${ftp.action.data.windows.home.path}")
    private String windowsPath;

    //@Scheduled(cron = "0 15 3 * * *")
    @Scheduled(cron = "0/20 * * * * *")
    public void parsingTask() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat date = new SimpleDateFormat("yyyy_MM_dd");
        StringBuilder fileHomePath = getHomePath();
        slackNotification.sendMessageToNotificationsChannel("_*Parsing start:*_");
        List<String> synonyms = chainRepository.getAllSynonyms();
        for (String chainSynonym : synonyms) {
            slackNotification.sendMessageToNotificationsChannel("*Scan folder:* _" + chainSynonym + "_");
            StringBuilder currentChainFolderPath = new StringBuilder(fileHomePath);
            currentChainFolderPath.append(chainSynonym)
                    .append(File.separator)
                    .append(actionDataFolder)
                    .append(File.separator);
            File folder = new File(currentChainFolderPath.toString());
            File[] folderFiles = folder.listFiles();
            if (folderFiles != null) {
                for (File file : folderFiles) {
                    Pattern pattern = Pattern.compile(".*" + date.format(calendar.getTime()) + "\\.xlsx");
                    Matcher matcher = pattern.matcher(file.getName());
                    if (matcher.find()) {
                        slackNotification.sendMessageToNotificationsChannel("Next file was parsed: `" + file.getName() + "`");
                        List<ParsingResult<?>> parsingResults = dataParser.parseActionProducts(file.getPath(), chainSynonym);
                        parsingResults.forEach((ParsingResult<?> result) ->
                                slackNotification.sendMessageToNotificationsChannel("```" + result.toString() + "```"));
                    } else {
                        slackNotification.sendMessageToNotificationsChannel("Next file was skipped: " + file.getName());
                    }
                }
            }
        }
    }

    private StringBuilder getHomePath() {
        StringBuilder homePath = new StringBuilder();
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().startsWith("windows")) {
            homePath.append(windowsPath)
                    .append(File.separator);
        } else if (osName.toLowerCase().startsWith("linux")) {
            homePath.append(linuxPath)
                    .append(File.separator);
        }
        return homePath;
    }
}
