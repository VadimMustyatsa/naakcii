package naakcii.by.api.chainstatistics;

import java.util.Calendar;
import java.util.List;

public interface ChainStatisticsService {

    ChainStatisticsDTO getChainStatistics(Long id);

    List<ChainStatisticsDTO> getAllChainsStatistics();

    ChainStatisticsDTO updateChainStatistics(Long id,
                                             Integer discountedProducts,
                                             Integer averageDiscountPercentage,
                                             Calendar creationDate);
}
