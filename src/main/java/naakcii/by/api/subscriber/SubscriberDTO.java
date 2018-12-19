package naakcii.by.api.subscriber;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class SubscriberDTO {

    private Long id;
    private String email;

    public SubscriberDTO(Subscriber subscriber) {
        this.id = subscriber.getId();
        this.email = subscriber.getEmail();
    }
}
