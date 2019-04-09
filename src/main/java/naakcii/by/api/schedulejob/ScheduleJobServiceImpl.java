package naakcii.by.api.schedulejob;

import com.google.common.collect.Lists;
import naakcii.by.api.util.scheduler.model.QuartzCronJob;
import org.quartz.Job;
import org.quartz.JobKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

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
        return Lists.newArrayList(scheduleJobRepository.findAll()).stream()
                .filter(Objects::nonNull)
                .map((ScheduleJob s) -> {
                    String beanName = s.getScheduleJobType().getBeanName().trim();
                    Job job = (Job) applicationContext.getBean(beanName);
                    String jobExpression = s.getCronExpression();
                    String jobName = s.getName();
                    String jobGroup = s.getScheduleJobType().getName();
                    return new QuartzCronJob(job, jobName, jobGroup, jobExpression);
                })
                .collect(Collectors.toList());
    }

    @Override
    public Set<JobKey> getAllJobsName() {
        return scheduleJobRepository.findAllName().stream()
                .filter(Objects::nonNull)
                .map(JobKey::new)
                .collect(Collectors.toSet());
    }
}
