package naakcii.by.api.statistic;

import org.springframework.data.repository.CrudRepository;

public interface StatisticRepository extends CrudRepository<Statistic, Long> {

    Statistic findTopByOrOrderByIdDesc();
}
