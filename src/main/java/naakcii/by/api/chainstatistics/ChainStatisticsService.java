package naakcii.by.api.chainstatistics;

import java.util.Calendar;
import java.util.List;

public interface ChainStatisticsService {

    ChainStatisticsDTO getChainStatistics(Integer id);

    List<ChainStatisticsDTO> getAllChainsStatistics();

    ChainStatisticsDTO updateChainStatistics(Integer chainId,
                                             Integer discountedProducts,
                                             Integer averageDiscountPercentage,
                                             Calendar creationDate);
}
