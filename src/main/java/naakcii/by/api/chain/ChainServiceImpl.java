package naakcii.by.api.chain;

import com.vaadin.flow.component.notification.Notification;
import naakcii.by.api.chainproduct.ChainProduct;
import naakcii.by.api.service.CrudService;
import naakcii.by.api.util.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ChainServiceImpl implements ChainService, CrudService<ChainDTO> {

    private final ChainRepository chainRepository;
    private final ObjectFactory objectFactory;
    
    @Autowired
    public ChainServiceImpl(ChainRepository chainRepository, ObjectFactory objectFactory) {
    	this.chainRepository = chainRepository;
    	this.objectFactory = objectFactory;
    }

    @Override
    public List<ChainDTO> findAllDTOs() {
        return chainRepository.findAllByOrderByName()
                .stream()
                .filter(Objects::nonNull)
                .map((Chain chain) -> objectFactory.getInstance(ChainDTO.class, chain))
                .collect(Collectors.toList());
    }

    @Override
    public List<ChainDTO> searchName(String name) {
        return chainRepository.findAllByNameContainingIgnoreCase(name)
                .stream()
                .filter(Objects::nonNull)
                .map((Chain chain) -> objectFactory.getInstance(ChainDTO.class, chain))
                .collect(Collectors.toList());
    }

    @Override
    public ChainDTO createNewDTO() {
        return new ChainDTO();
    }

    @Override
    public ChainDTO saveDTO(ChainDTO chainDTO) {
        if (chainDTO.getId() == null) {
            if (chainRepository.findBySynonymIgnoreCase(chainDTO.getSynonym()).isPresent()) {
                Notification.show("Данная торговая сеть уже внесена в базу");
                return null;
            }
            return new ChainDTO(chainRepository.save(new Chain(chainDTO)));
        } else {
            return new ChainDTO(chainRepository.save(new Chain(chainDTO)));
        }
    }

    @Override
    @Transactional
    public void deleteDTO(ChainDTO entityDTO) {
        Optional<Chain> chain = chainRepository.findBySynonymIgnoreCase(entityDTO.getSynonym());
            if (!chain.isPresent()) {
            throw new EntityNotFoundException();
        } else {
            Set<ChainProduct> chainProducts = chain.get().getChainProducts();
            if(chainProducts.size()>0) {
                Notification.show("Эта торговая сеть присвоена " + chainProducts.size() + " товарам");
                throw new RuntimeException("Удаление невозможно");
            } else {
                chainRepository.delete(chain.get());
            }
        }
    }
}
