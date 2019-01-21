package naakcii.by.api.statistics;

import java.util.Calendar;

public interface StatisticsService {

    StatisticsDTO findCurrentStatistics();

    Statistics updateStatistics(Integer chainQuantity,
                                Integer discountedProducts,
                                Integer averageDiscountPercentage,
                                Calendar creationDate);
}
