package naakcii.by.api.viber.service.modelDTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
public class ViberSenderDTO {
    @Value("${viber.sender.name}")
    public String name;
    @Value("${viber.sender.avatar}")
    public String avatar;
}
