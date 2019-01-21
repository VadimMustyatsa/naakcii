package naakcii.by.api.statistics;

import org.springframework.data.repository.CrudRepository;

public interface StatisticsRepository extends CrudRepository<Statistics, Long> {

    Statistics findFirstByOrderByIdAsc();

}
