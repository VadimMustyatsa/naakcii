package naakcii.by.api.schedulejob;

import com.google.common.collect.Lists;
import naakcii.by.api.util.scheduler.model.QuartzCronJob;
import org.quartz.Job;
import org.quartz.JobKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ScheduleJobServiceImpl implements ScheduleJobService {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ScheduleJobRepository scheduleJobRepository;

    public List<QuartzCronJob> getAllJobs() {
        List<QuartzCronJob> jobList = new ArrayList<>();
        List<ScheduleJob> dbJobList = Lists.newArrayList(scheduleJobRepository.findAll());

        for (ScheduleJob scheduleJob : dbJobList) {
            String className = scheduleJob.getScheduleJobType().getClassName().trim();
            Job job = (Job) applicationContext.getBean(className);
            String jobExpression = scheduleJob.getCronExpression();
            String jobName = scheduleJob.getName();
            String jobGroup = scheduleJob.getScheduleJobType().getName();
            jobList.add(new QuartzCronJob(job, jobName, jobGroup, jobExpression));
        }
        return jobList;
    }

    @Override
    public Set<JobKey> getAllJobsName() {
        return scheduleJobRepository.findAllName().stream()
                .filter(Objects::nonNull)
                .map(JobKey::new)
                .collect(Collectors.toSet());
    }
}
