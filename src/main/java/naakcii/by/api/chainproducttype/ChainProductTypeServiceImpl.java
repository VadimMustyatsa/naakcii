package naakcii.by.api.chainproducttype;

import com.vaadin.flow.component.notification.Notification;
import naakcii.by.api.chainproduct.ChainProduct;
import naakcii.by.api.chainproduct.ChainProductRepository;
import naakcii.by.api.service.CrudService;
import naakcii.by.api.util.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChainProductTypeServiceImpl implements CrudService<ChainProductTypeDTO>, ChainProductTypeService {

    private final ChainProductTypeRepository chainProductTypeRepository;
    private final ChainProductRepository chainProductRepository;
    private final ObjectFactory objectFactory;

    @Autowired
    public ChainProductTypeServiceImpl(ChainProductTypeRepository chainProductTypeRepository,
                                       ChainProductRepository chainProductRepository,
                                       ObjectFactory objectFactory) {
        this.chainProductTypeRepository = chainProductTypeRepository;
        this.chainProductRepository = chainProductRepository;
        this.objectFactory = objectFactory;
    }

    @Override
    public List<ChainProductTypeDTO> findAllDTOs() {
        return chainProductTypeRepository.findAllByOrderByName()
                .stream()
                .filter(Objects::nonNull)
                .map((ChainProductType chainProductType) -> objectFactory.getInstance(ChainProductTypeDTO.class, chainProductType))
                .collect(Collectors.toList());
    }

    @Override
    public List<ChainProductTypeDTO> searchName(String name) {
        return chainProductTypeRepository.findAllByNameContainingIgnoreCase(name)
                .stream()
                .filter(Objects::nonNull)
                .map((ChainProductType chainProductType) -> objectFactory.getInstance(ChainProductTypeDTO.class, chainProductType))
                .collect(Collectors.toList());
    }

    @Override
    public ChainProductTypeDTO createNewDTO() {
        return new ChainProductTypeDTO();
    }

    @Override
    public ChainProductTypeDTO saveDTO(ChainProductTypeDTO entityDTO) {
        Optional<ChainProductType> chainProductType = chainProductTypeRepository
                .findByNameIgnoreCaseAndSynonymIgnoreCase(entityDTO.getName(), entityDTO.getSynonym());
        ChainProductType chainProductTypeDB = chainProductTypeRepository.findByNameIgnoreCase(entityDTO.getName());
        if(entityDTO.getId()==null) {
            if (chainProductType.isPresent() || chainProductTypeDB!=null) {
                Notification.show("Данная акция уже внесена в базу");
                return null;
            }
            return new ChainProductTypeDTO(chainProductTypeRepository.save(new ChainProductType(entityDTO)));
        } else {
            return new ChainProductTypeDTO(chainProductTypeRepository.save(new ChainProductType(entityDTO)));
        }
    }

    @Override
    public void deleteDTO(ChainProductTypeDTO entityDTO) {
        Optional<ChainProductType> chainProductType = chainProductTypeRepository.findById(entityDTO.getId());
        if (!chainProductType.isPresent()) {
            throw new EntityNotFoundException();
        } else {
            List<ChainProduct> chainProducts = chainProductRepository.findAllByTypeId(entityDTO.getId());
            if(chainProducts.size()>0) {
                Notification.show("Данный тип акции присвоен " + chainProducts.size() + " товарам");
                throw new RuntimeException("Удаление невозможно");
            } else {
                chainProductTypeRepository.delete(chainProductType.get());
            }
        }
    }

    @Override
    public List<String> getAllChainProductTypeNames() {
        return chainProductTypeRepository.findAllByOrderByName()
                .stream()
                .map(ChainProductType::getName)
                .collect(Collectors.toList());
    }
}
