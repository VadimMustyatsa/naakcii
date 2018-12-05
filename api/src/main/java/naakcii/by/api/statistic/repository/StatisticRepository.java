package naakcii.by.api.statistic.repository;

import naakcii.by.api.statistic.repository.model.Statistic;
import org.springframework.data.repository.CrudRepository;

public interface StatisticRepository extends CrudRepository<Statistic, Long> {
    Statistic findById(int i);
}
