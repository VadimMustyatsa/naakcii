package naakcii.by.api.statistics.service.util;

import naakcii.by.api.statistics.repository.model.Statistics;
import naakcii.by.api.statistics.service.modelDTO.StatisticsDTO;
import org.springframework.stereotype.Component;

@Component
public class StatisticsConverter {

    public StatisticsDTO convert(Statistics statistics) {
        StatisticsDTO statisticsDTO = new StatisticsDTO();
        statisticsDTO.setChainQuantity(statistics.getChainQuantity());
        statisticsDTO.setAverageDiscount(statistics.getAverageDiscount());
        statisticsDTO.setDiscountedGoods(statistics.getDiscountedGoods());
        statisticsDTO.setId(statistics.getId());
        return statisticsDTO;
    }
}
