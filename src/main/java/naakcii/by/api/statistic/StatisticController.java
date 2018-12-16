package naakcii.by.api.statistic;

import naakcii.by.api.config.ApiConfigConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/products"})
public class StatisticController {

    @Autowired
    private StatisticService statisticService;

    @GetMapping(produces = ApiConfigConstants.API_V_2_0)
    public StatisticDTO getStatistic() {
        return statisticService.findCurrentStatistic();
    }
}
