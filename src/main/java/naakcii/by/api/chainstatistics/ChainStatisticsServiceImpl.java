package naakcii.by.api.chainstatistics;

import naakcii.by.api.chain.Chain;
import naakcii.by.api.chain.ChainRepository;
import naakcii.by.api.util.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChainStatisticsServiceImpl implements ChainStatisticsService {

    private ChainStatisticsRepository chainStatisticsRepository;
    private ChainRepository chainRepository;
    private ObjectFactory objectFactory;

    @Autowired
    public ChainStatisticsServiceImpl(ChainStatisticsRepository chainStatisticsRepository, ObjectFactory objectFactory,
                                      ChainRepository chainRepository) {
        this.chainStatisticsRepository = chainStatisticsRepository;
        this.objectFactory = objectFactory;
        this.chainRepository = chainRepository;
    }

    @Override
    public ChainStatisticsDTO getChainStatistics(Long id) {
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
    @Transactional
    public ChainStatisticsDTO updateChainStatistics(Long id, Integer discountedProducts, Integer averageDiscountPercentage, Calendar creationDate) {
        Optional<ChainStatistics> chainStatisticsOptional = chainStatisticsRepository.findById(id);
        Optional<Chain> chainOptional = chainRepository.findById(id);
        ChainStatistics chainStatistics = null;

        if (chainOptional.isPresent()) {
            Chain chain = chainOptional.get();

            if (chainStatisticsOptional.isPresent()) {
                chainStatistics = chainStatisticsOptional.get();
                chainStatistics.setDiscountedProducts(discountedProducts);
                chainStatistics.setAverageDiscountPercentage(averageDiscountPercentage);
                chainStatistics.setCreationDate(creationDate);
            } else {
                chainStatistics = new ChainStatistics(discountedProducts, averageDiscountPercentage, creationDate);
            }

            chainStatistics.setChain(chain);
            chain.setChainStatistics(chainStatistics);
            chainStatistics = chainStatisticsRepository.save(chainStatistics);
        }
        return objectFactory.getInstance(ChainStatisticsDTO.class, chainStatistics);
    }
}
