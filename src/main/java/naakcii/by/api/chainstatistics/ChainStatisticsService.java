package naakcii.by.api.chainstatistics;

import java.util.Calendar;

public interface ChainStatisticsService {

    ChainStatisticsDTO updateChainStatistics(Long id,
                                             Integer discountedProducts,
                                             Integer averageDiscountPercentage,
                                             Calendar creationDate);
}
