package naakcii.by.api.action.service.util;

import naakcii.by.api.action.repository.model.Action;
import naakcii.by.api.action.service.modelDTO.ActionDTO;
import org.springframework.stereotype.Component;

@Component
public class ActionConverter {

    public ActionDTO converter(Action action) {
        ActionDTO actionDTO = new ActionDTO();
        actionDTO.setProductId(action.getProduct().getId());
        actionDTO.setDiscount(action.getDiscount());
        actionDTO.setPrice(action.getPrice());
        actionDTO.setType(action.getType());
        actionDTO.setChainId(action.getChain().getId());
        actionDTO.setDiscountPrice(action.getDiscountPrice());
        return actionDTO;
    }
}
