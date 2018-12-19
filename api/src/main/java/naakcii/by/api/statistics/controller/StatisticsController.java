package naakcii.by.api.statistics.controller;

import naakcii.by.api.statistics.service.StatisticsService;
import naakcii.by.api.statistics.service.modelDTO.StatisticsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

@RequestMapping({"/statistics"})
public class StatisticsController {

    @Autowired
    StatisticsService statisticsService;


    @GetMapping
    public StatisticsDTO getStatistic() {
        return (statisticsService.getStatistics());
    }
}



