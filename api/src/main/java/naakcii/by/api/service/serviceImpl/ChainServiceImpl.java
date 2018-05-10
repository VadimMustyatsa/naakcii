package naakcii.by.api.service.ServiceImpl;

import naakcii.by.api.repository.dao.ActionDao;
import naakcii.by.api.repository.dao.ChainDao;
import naakcii.by.api.repository.model.Action;
import naakcii.by.api.repository.model.Chain;
import naakcii.by.api.service.ChainService;
import naakcii.by.api.service.modelDTO.ChainDTO;
import naakcii.by.api.service.util.ChainConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ChainServiceImpl implements ChainService {

    private ChainConverter chainConverter = new ChainConverter();

    @Autowired
    private ChainDao repository;

    @Autowired
    private ActionDao actionDao;

    @Override
    public List<ChainDTO> findAll() {
        List<Chain> chainList = repository.findAll();
        List<ChainDTO> chainDTOList = new ArrayList<ChainDTO>();
        for (Chain chain : chainList) {
            chainDTOList.add(chainConverter.convert(chain));
        }
        for (ChainDTO chainDTO : chainDTOList) {
            Map<String, Integer> chainProperties = getChainProperties(chainDTO);
            chainDTO.setCountGoods(chainProperties.get("countGoods"));
            chainDTO.setPercent(chainProperties.get("discountSize"));
        }
        Collections.sort(chainDTOList, Comparator.comparing(ChainDTO::getName));
        return chainDTOList;
    }

    @Override
    public Map<String, Integer> getChainProperties(ChainDTO chainDTO) {
        List<Action> actionList = actionDao.findByChainId(chainDTO.getId());
        Map<String, Integer> chainProperties = new HashMap<String, Integer>();
        chainProperties.put("countGoods", actionList.size());
        Integer count = 0;
        Double discountCount = 0.00;
        for (Action action : actionList) {
            if (action.getDiscount() > 0) {
                count++;
                discountCount = discountCount + action.getDiscount();
            }
        }
        Integer discountSize = (int) (discountCount / count);
        chainProperties.put("discountSize", discountSize);
        return chainProperties;
    }
}
