package naakcii.by.api.schedulejob;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface ScheduleJobRepository extends CrudRepository<ScheduleJob, Integer> {

    @Query("select sj.name from ScheduleJob sj")
    Set<String> findAllName();

}
