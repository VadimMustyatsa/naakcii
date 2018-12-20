package naakcii.by.api.statistics.repository;

import naakcii.by.api.statistics.repository.model.Statistics;
import org.springframework.data.repository.CrudRepository;

public interface StatisticsRepository extends CrudRepository<Statistics, Long> {
    Statistics findOneById(Long id);
}
