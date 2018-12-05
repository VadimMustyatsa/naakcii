package naakcii.by.api.chain.service.serviceImpl;

import naakcii.by.api.action.service.ActionService;
import naakcii.by.api.chain.repository.ChainRepository;
import naakcii.by.api.chain.repository.model.Chain;
import naakcii.by.api.chain.service.ChainService;
import naakcii.by.api.chain.service.modelDTO.ChainDTO;
import naakcii.by.api.chain.service.util.ChainConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ChainServiceImpl implements ChainService {

    private ChainConverter chainConverter = new ChainConverter();

    @Autowired
    private ChainRepository chainRepository;

    @Autowired
    private ActionService actionService;

    @Override
    public List<ChainDTO> findAll() {
        List<Chain> chainList = chainRepository.findAllByOrderByNameAsc();
        List<ChainDTO> chainDTOList = new ArrayList<>();
        for (Chain chain : chainList) {
            ChainDTO chainDTO = chainConverter.convert(chain);
            List<Integer> results = actionService.getDiscountAndAllActionsByChain(chainDTO);
            if(results.size() != 0) {
                chainDTO.setCountGoods(results.get(0));
                chainDTO.setPercent(results.get(1));
            }
            chainDTOList.add(chainDTO);
        }

        return chainDTOList;
    }
}
