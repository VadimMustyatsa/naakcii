package naakcii.by.api.action.service.serviceImpl;

import org.springframework.stereotype.Service;

import naakcii.by.api.action.service.ActionService;

@Service
public class ActionServiceImpl implements ActionService {
/*
    @Autowired
    ActionRepository repository;

    private ChainConverter chainConverter = new ChainConverter();

    @Override
    public List<Integer> getDiscounAndAllActionsByChain(ChainDTO chainDTO) {
        Integer discount = 0;
        int number = 0;
        List<Integer> result = new ArrayList<>();
        Chain chain = chainConverter.convert(chainDTO);
        List<Action> actions = repository.findAllByChain(chain);
        result.add(actions.size());
        for (Action action : actions) {
            if (action.getPrice() != 0) {
                if (action.getPrice() > action.getDiscountPrice()) {
                    number = number + 1;
                    discount = discount + action.getDiscount();
                }
            }
        }
        discount = discount / number;
        result.add(discount);
        return result;
    }*/
}
