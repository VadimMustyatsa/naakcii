package naakcii.by.api.statistics;

import naakcii.by.api.util.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private StatisticsRepository statisticsRepository;

    @Autowired
    private ObjectFactory objectFactory;

    @Override
    public StatisticsDTO findCurrentStatistics() {
        Statistics statistics = statisticsRepository.findFirstByOrderByIdAsc();
        return objectFactory.getInstance(StatisticsDTO.class, statistics);
    }

    @Override
    public Statistics updateStatistics(Integer chainQuantity,
                                       Integer discountedProducts,
                                       Integer averageDiscountPercentage,
                                       Calendar creationDate) {
        Statistics statistics = statisticsRepository.findFirstByOrderByIdAsc();
        statistics.setChainQuantity(chainQuantity);
        statistics.setDiscountedProducts(discountedProducts);
        statistics.setAverageDiscountPercentage(averageDiscountPercentage);
        statistics.setCreationDate(creationDate);
        return statisticsRepository.save(statistics);
    }
}
