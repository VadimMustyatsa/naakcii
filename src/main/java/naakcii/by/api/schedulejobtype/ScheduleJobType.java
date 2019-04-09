package naakcii.by.api.schedulejobtype;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(exclude = {"id"})
@Entity
@Table(name = "SCHEDULE_JOB_TYPE")
public class ScheduleJobType implements Serializable {

    private static final long serialVersionUID = -397384728748404791L;

    @Id
    @NotNull
    @Column(name = "SCHEDULE_JOB_TYPE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Byte id;

    @Column(name = "SCHEDULE_JOB_TYPE_NAME")
    private String name;

    @Column(name = "SCHEDULE_JOB_TYPE_BEAN_NAME")
    private String beanName;

}
