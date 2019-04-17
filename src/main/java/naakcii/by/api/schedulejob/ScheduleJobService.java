package naakcii.by.api.schedulejob;

import naakcii.by.api.util.scheduler.model.QuartzCronJob;
import org.quartz.JobKey;

import java.util.List;
import java.util.Set;

public interface ScheduleJobService {

    List<QuartzCronJob> getAllJobs();

    Set<JobKey> getAllJobsName();

}
