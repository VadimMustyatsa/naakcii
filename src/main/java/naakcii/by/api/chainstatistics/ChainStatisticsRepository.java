package naakcii.by.api.chainstatistics;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ChainStatisticsRepository extends CrudRepository<ChainStatistics, Integer> {

    Optional<ChainStatistics> findById(Integer chainId);

    List<ChainStatistics> getAllByChain_IsActiveTrueOrderByChain_NameAsc();
}
