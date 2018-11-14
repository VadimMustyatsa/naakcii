package naakcii.by.api.action.service.serviceImpl;

import naakcii.by.api.action.repository.ActionRepository;
import naakcii.by.api.action.repository.model.Action;
import naakcii.by.api.action.service.ActionService;
import naakcii.by.api.chain.repository.model.Chain;
import naakcii.by.api.chain.service.modelDTO.ChainDTO;
import naakcii.by.api.chain.service.util.ChainConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ActionServiceImpl implements ActionService {

    @Autowired
    ActionRepository repository;
    @Autowired
    private ChainConverter chainConverter;

    @Override
    public List<Integer> getDiscountAndAllActionsByChain(ChainDTO chainDTO) {
        Integer discount = 0;
        int number = 0;
        List<Integer> result = new ArrayList<>();
        Chain chain = chainConverter.convert(chainDTO);
        List<Action> actions = repository.findAllByChain(chain);
        result.add(actions.size());
        for (Action action : actions) {
            if (action.getPrice() > action.getDiscountPrice()) {
                number = number + 1;
                discount = discount + action.getDiscount();
            }
        }
        discount = discount / number;
        result.add(discount);
        return result;
    }
}
