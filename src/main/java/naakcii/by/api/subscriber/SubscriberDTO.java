package naakcii.by.api.subscriber;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class SubscriberDTO {

    @ApiModelProperty(notes = "Id подписчика", example="1L")
    private Long id;
    @ApiModelProperty(notes = "Электронный адрес подписчика", example="example@gmail.com")
    private String email;

    public SubscriberDTO(Subscriber subscriber) {
        this.id = subscriber.getId();
        this.email = subscriber.getEmail();
    }
}
