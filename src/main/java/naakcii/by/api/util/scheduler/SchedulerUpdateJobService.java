package naakcii.by.api.util.scheduler;

import naakcii.by.api.schedulejob.ScheduleJobService;
import naakcii.by.api.util.scheduler.jobs.AllChainDataParserJob;
import naakcii.by.api.util.scheduler.model.QuartzCronJob;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class SchedulerUpdateJobService {

    private static final Logger logger = LogManager.getLogger(SchedulerUpdateJobService.class);

    private ScheduleJobService scheduleJobService;

    private Scheduler scheduler;


    @Autowired
    public SchedulerUpdateJobService(@Qualifier("schedulerBeanFactory") SchedulerFactoryBean schedulerFactory,
                                     ScheduleJobService scheduleJobService) {
        this.scheduleJobService = scheduleJobService;
        this.scheduler = schedulerFactory.getScheduler();
    }

    @Scheduled(cron = "${scheduler.update.cron}")
    public void updateQuartzJobs() {
        try {
            Set<JobKey> schedulerJobKeys = scheduler.getJobKeys(GroupMatcher.anyGroup());
            List<QuartzCronJob> databaseJobs = scheduleJobService.getAllJobs();
            logger.info("`Quartz` jobs update was started");
            logger.info("Current `Quartz` jobs: " + schedulerJobKeys.toString());

            for (QuartzCronJob databaseJob : databaseJobs) {
                JobKey jobKey = new JobKey(databaseJob.getName());

                if (!schedulerJobKeys.contains(jobKey)) {
                    scheduleJob(databaseJob, jobKey);
                    logger.info("`Quartz` job was added: " + jobKey.toString()
                            + " `" + databaseJob.getExpression() + "`");
                } else {
                    CronTrigger trigger = (CronTrigger) scheduler.getTriggersOfJob(jobKey).get(0);
                    String cronExpression = trigger.getCronExpression();
                    if (!Objects.equals(cronExpression, databaseJob.getExpression())) {
                        scheduler.deleteJob(jobKey);
                        scheduleJob(databaseJob, jobKey);
                        logger.info("`Quartz` job cron was updated: " + jobKey.toString()
                                + "  `" + cronExpression + "` -> `" + databaseJob.getExpression() + "`");
                    }
                }
            }

            removeDeletedJobs();
            logger.info("`Quartz` jobs update was finished");
        } catch (SchedulerException e) {
            logger.error("`Quartz` jobs update was terminated: " + e.getMessage());
        }
    }

    private void removeDeletedJobs() throws SchedulerException {
        Set<JobKey> schedulerJobKeys = scheduler.getJobKeys(GroupMatcher.anyGroup());
        Set<JobKey> dbJobsName = scheduleJobService.getAllJobsName();
        for (JobKey schedulerJobKey : schedulerJobKeys) {
            if (!dbJobsName.contains(schedulerJobKey)) {
                scheduler.deleteJob(schedulerJobKey);
                logger.info("`Quartz` jobs update was delete: " + schedulerJobKey);
            }
        }
    }

    private void scheduleJob(QuartzCronJob databaseJob, JobKey jobKey) {
        try {
            JobDetail jobDetail = JobBuilder.newJob()
                    .ofType(AllChainDataParserJob.class)
                    .withIdentity(jobKey).build();
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withSchedule(CronScheduleBuilder.cronSchedule(databaseJob.getExpression()))
                    .build();
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            logger.error(e.getMessage());
        }
    }
}
