package naakcii.by.api.util.tasks;

import naakcii.by.api.chain.ChainRepository;
import naakcii.by.api.chainproduct.ChainProductRepository;
import naakcii.by.api.product.ProductRepository;
import naakcii.by.api.statistics.StatisticsService;
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
import java.math.BigDecimal;
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
    private ProductRepository productRepository;
    private ChainProductRepository chainProductRepository;
    private StatisticsService statisticsService;
    private DataParser dataParser;

    @Value("${ftp.action.data.path}")
    private String dataPath;

    @Value("${ftp.action.data.folder.name}")
    private String dataFolder;

    @Value("${ftp.action.log.path}")
    private String logFolder;

    @Autowired
    public DataParserTask(SlackNotification slackNotification, ChainRepository chainRepository,
                          ProductRepository productRepository, DataParser dataParser,
                          StatisticsService statisticsService, ChainProductRepository chainProductRepository) {
        this.slackNotification = slackNotification;
        this.chainRepository = chainRepository;
        this.dataParser = dataParser;
        this.productRepository = productRepository;
        this.chainProductRepository = chainProductRepository;
        this.statisticsService = statisticsService;
    }

    @Scheduled(cron = "0 15 3 * * *")
    public void parsingTask() {
        logger.info("Parsing start:");
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat date = new SimpleDateFormat("yyyy_MM_dd");
        StringBuilder fileHomePath = new StringBuilder(dataPath);
        slackNotification.sendMessageToNotificationsChannel("_*Parsing start:*_");
        List<String> synonyms = chainRepository.getAllSynonyms();
        slackNotification.sendMessageToNotificationsChannel("_ List of chains: _" + synonyms);

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
                                    saveAndNotifyParsingResult(result.toString(), file.getName(),
                                            result.getTargetClass().getSimpleName()));
                        } else {
                            slackNotification.sendMessageToNotificationsChannel("Next file was skipped: `" + file.getName() + "`");
                            logger.info("Next file was skipped: " + file.getName());
                        }
                    }
                }
            } catch (Exception e) {
                slackNotification.sendMessageToNotificationsChannel("Parsing task was terminated: " + e.getMessage());
                logger.info(e.getMessage());
            }
        }
        calculateAndUpdateStatistics();
    }

    private void saveAndNotifyParsingResult(String result, String filename, String targetClass) {
        String logFileName = filename.replaceAll("\\.", "_");
        File file = new File(logFolder + logFileName + "_" + targetClass.toLowerCase() + ".log");

        try (
                Writer writer = new FileWriter(file)
        ) {
            writer.write(result);
            writer.flush();
        } catch (Exception e) {
            logger.error(e.getMessage());
            slackNotification.sendMessageToNotificationsChannel(e.getMessage());
        }
        slackNotification.sendCodeSnippetToNotificationsChannel(file);
    }

    public void calculateAndUpdateStatistics() {
        Integer chains = chainRepository.countChainsByIsActiveTrue();
        Integer products = productRepository.countProductsByIsActiveTrue();
        BigDecimal averageDiscountPercentage = chainProductRepository.findAverageDiscountPercentage();
        Calendar calendar = Calendar.getInstance();

        if (chains != null && products != null && averageDiscountPercentage != null) {
            statisticsService.updateStatistics(chains, products, averageDiscountPercentage.intValue(), calendar);
            slackNotification.sendMessageToNotificationsChannel("*Statistics was updated:*"
                    + " `chains:` " + chains
                    + " `products:` " + products
                    + " `average discount percentage:` " + averageDiscountPercentage.intValue());
        } else {
            slackNotification.sendMessageToNotificationsChannel("Statistics update was terminated: NULL value");
        }
    }
}
