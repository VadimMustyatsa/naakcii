package naakcii.by.api.util.scheduler.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.quartz.Job;

@NoArgsConstructor
@Getter
@Setter
public class QuartzCronJob {

    private Job job;
    private String name;
    private String group;
    private String expression;

    public QuartzCronJob(Job job, String name, String group, String expression) {
        this.job = job;
        this.name = name;
        this.group = group;
        this.expression = expression;
    }


}
