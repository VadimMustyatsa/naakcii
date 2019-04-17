package naakcii.by.api.chainstatistics;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ChainStatisticsRepository extends CrudRepository<ChainStatistics, Integer> {

    Optional<ChainStatistics> findById(Long chainId);

    @Query("select cs from ChainStatistics cs where chain.isActive = true order by chain.name asc")
    List<ChainStatistics> getAllByChain_IsActiveTrueOrderByChain_NameAsc();
}
