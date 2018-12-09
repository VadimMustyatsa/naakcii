package naakcii.by.api.subscriber.repository.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import java.io.Serializable;

@NoArgsConstructor
@Data
@Entity
@Table(name = "SUBSCRIBER")
public class Subscriber implements Serializable {

    private static final long serialVersionUID = -5695859545586187219L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "SUBSCRIBER_EMAIL")
    @Email(message = "Email should be valid")
    private String email;

}
