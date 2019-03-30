package naakcii.by.api.util.tasks;

import naakcii.by.api.chain.Chain;
import naakcii.by.api.chain.ChainRepository;
import naakcii.by.api.chainproduct.ChainProductRepository;
import naakcii.by.api.chainstatistics.ChainStatisticsDTO;
import naakcii.by.api.chainstatistics.ChainStatisticsService;
import naakcii.by.api.statistics.StatisticsDTO;
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
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DataParserTask {

    private static final Logger logger = LogManager.getLogger(DataParserTask.class);
    private SlackNotification slackNotification;
    private ChainRepository chainRepository;
    private ChainProductRepository chainProductRepository;
    private ChainStatisticsService chainStatisticsService;
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
                          DataParser dataParser, StatisticsService statisticsService,
                          ChainProductRepository chainProductRepository, ChainStatisticsService chainStatisticsService) {
        this.slackNotification = slackNotification;
        this.chainRepository = chainRepository;
        this.dataParser = dataParser;
        this.chainProductRepository = chainProductRepository;
        this.statisticsService = statisticsService;
        this.chainStatisticsService = chainStatisticsService;
    }

    @Scheduled(cron = "${by.naakcii.api.util.tasks.data.parser.task}")
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
                slackNotification.sendMessageToNotificationsChannel("*Parsing task was terminated:* ```" + e.getMessage() + "```");
                logger.error(e.getMessage());
            }
            calculateAndUpdateChainStatistics(chainSynonym);
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

    private void calculateAndUpdateStatistics() {
        int chains = chainRepository.countChainsByIsActiveTrue();
        int products = chainProductRepository.countChainProductByProduct_IsActiveTrue();
        BigDecimal averageDiscountPercentage = chainProductRepository.findAverageDiscountPercentage();

        if (chains != 0 && products != 0 && averageDiscountPercentage != null) {
            StatisticsDTO statisticsDTO = statisticsService.updateStatistics(
                    chains,
                    products,
                    averageDiscountPercentage.intValue(),
                    Calendar.getInstance());
            logger.info("Updated statistics - " + statisticsDTO);
            slackNotification.sendMessageToNotificationsChannel("*Total statistics:*"
                    + " `chains:` " + chains
                    + " `discounted products:` " + products
                    + " `average discount percentage:` " + averageDiscountPercentage.intValue());
        } else {
            StringBuilder message = new StringBuilder("*Statistics update was terminated*: ");
            if (chains == 0) {
                message.append("`chains - 0` ");
            }
            if (products == 0) {
                message.append("`products - 0` ");
            }
            if (averageDiscountPercentage == null) {
                message.append("`average discount percentage - NULL` ");
            }
            slackNotification.sendMessageToNotificationsChannel(message.toString());
        }
    }

    private void calculateAndUpdateChainStatistics(String chainSynonym) {
        int chainDiscountedProducts =
                chainProductRepository.countChainProductByProduct_IsActiveTrueAndChain_Synonym(chainSynonym);
        BigDecimal chainAvgDiscountPercentage =
                chainProductRepository.findAverageDiscountPercentageByChainIdSynonym(chainSynonym);
        Optional<Chain> chainOptional = chainRepository.findBySynonym(chainSynonym);
        Integer avgDiscountPercentage = 0;

        if (chainAvgDiscountPercentage != null) {
            avgDiscountPercentage = chainAvgDiscountPercentage.intValue();
        }

        if (chainOptional.isPresent()) {
            ChainStatisticsDTO chainStatisticsDTO = chainStatisticsService.updateChainStatistics(chainOptional.get().getId(),
                    chainDiscountedProducts, avgDiscountPercentage, Calendar.getInstance());
            logger.info("Updated `" + chainSynonym + "` statistics - " + chainStatisticsDTO);
            slackNotification.sendMessageToNotificationsChannel("*_Statistics:_*"
                    + " `discounted products:` " + chainDiscountedProducts
                    + " `average discount percentage:` " + avgDiscountPercentage);
            slackNotification.sendMessageToNotificationsChannel("------------------------------------------");
        }
    }
}
