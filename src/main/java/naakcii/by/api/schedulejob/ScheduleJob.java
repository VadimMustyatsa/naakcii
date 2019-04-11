package naakcii.by.api.schedulejob;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naakcii.by.api.schedulejobtype.ScheduleJobType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(exclude = {"id"})
@Entity
@Table(name = "SCHEDULE_JOB")
public class ScheduleJob implements Serializable {

    private static final long serialVersionUID = -4053114041932518831L;

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SCHEDULE_JOB_ID")
    private Integer id;

    @NotNull
    @Column(name = "SCHEDULE_JOB_NAME", unique = true, updatable = false)
    private String name;

    @NotNull
    @Column(name = "SCHEDULE_JOB_CRON_EXPRESSION")
    private String cronExpression;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "SCHEDULE_JOB_TYPE_ID")
    ScheduleJobType scheduleJobType;

}
