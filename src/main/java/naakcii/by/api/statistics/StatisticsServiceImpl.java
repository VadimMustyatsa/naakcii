package naakcii.by.api.statistics;

import naakcii.by.api.util.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private StatisticsRepository statisticsRepository;
    private ObjectFactory objectFactory;

    @Autowired
    public StatisticsServiceImpl(StatisticsRepository statisticsRepository, ObjectFactory objectFactory) {
        this.statisticsRepository = statisticsRepository;
        this.objectFactory = objectFactory;
    }

    @Override
    public StatisticsDTO getCurrentStatistics() {
        Statistics statistics = statisticsRepository.findFirstByOrderByIdAsc();
        return objectFactory.getInstance(StatisticsDTO.class, statistics);
    }

    @Override
    public StatisticsDTO updateStatistics(Integer chainQuantity,
                                          Integer discountedProducts,
                                          Integer averageDiscountPercentage,
                                          Calendar creationDate) {
        Statistics statistics = statisticsRepository.findFirstByOrderByIdAsc();
        if (statistics == null) {
            statistics = new Statistics();
        }
        statistics.setChainQuantity(chainQuantity);
        statistics.setDiscountedProducts(discountedProducts);
        statistics.setAverageDiscountPercentage(averageDiscountPercentage);
        statistics.setCreationDate(creationDate);
        Statistics updatedStatistics = statisticsRepository.save(statistics);
        return objectFactory.getInstance(StatisticsDTO.class, updatedStatistics);
    }
}
