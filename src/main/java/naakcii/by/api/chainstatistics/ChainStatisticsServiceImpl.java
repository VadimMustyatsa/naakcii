package naakcii.by.api.chainstatistics;

import naakcii.by.api.util.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChainStatisticsServiceImpl implements ChainStatisticsService {

    private ChainStatisticsRepository chainStatisticsRepository;
    private ObjectFactory objectFactory;

    @Autowired
    public ChainStatisticsServiceImpl(ChainStatisticsRepository chainStatisticsRepository, ObjectFactory objectFactory) {
        this.chainStatisticsRepository = chainStatisticsRepository;
        this.objectFactory = objectFactory;
    }

    @Override
    public ChainStatisticsDTO getChainStatistics(Integer id) {
        Optional<ChainStatistics> chainStatisticsOptional = chainStatisticsRepository.findById(id);
        ChainStatisticsDTO chainStatisticsDTO = new ChainStatisticsDTO();
        if (chainStatisticsOptional.isPresent()) {
            chainStatisticsDTO = objectFactory.getInstance(ChainStatisticsDTO.class, chainStatisticsOptional.get());
        }
        return chainStatisticsDTO;
    }

    @Override
    public List<ChainStatisticsDTO> getAllChainsStatistics() {
        return chainStatisticsRepository.getAllByChain_IsActiveTrueOrderByChain_NameAsc()
                .stream()
                .filter(Objects::nonNull)
                .map((ChainStatistics statistics) -> objectFactory.getInstance(ChainStatisticsDTO.class, statistics))
                .collect(Collectors.toList());
    }

    @Override
    public ChainStatisticsDTO updateChainStatistics(Integer id, Integer discountedProducts, Integer averageDiscountPercentage, Calendar creationDate) {
        Optional<ChainStatistics> chainStatisticsOptional = chainStatisticsRepository.findById(id);
        ChainStatistics chainStatistics;
        if (chainStatisticsOptional.isPresent()) {
            chainStatistics = chainStatisticsOptional.get();
        } else {
            chainStatistics = new ChainStatistics();
            chainStatistics.setId(id);
        }
        chainStatistics.setDiscountedProducts(discountedProducts);
        chainStatistics.setAverageDiscountPercentage(averageDiscountPercentage);
        chainStatistics.setCreationDate(creationDate);
        chainStatisticsRepository.save(chainStatistics);
        return objectFactory.getInstance(ChainStatisticsDTO.class, chainStatistics);
    }
}
