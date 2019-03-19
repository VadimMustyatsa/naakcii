package naakcii.by.api.unitofmeasure;

import com.vaadin.flow.component.notification.Notification;
import naakcii.by.api.product.Product;
import naakcii.by.api.product.ProductRepository;
import naakcii.by.api.service.CrudService;
import naakcii.by.api.util.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService, CrudService<UnitOfMeasureDTO> {

    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final ObjectFactory objectFactory;
    private final ProductRepository productRepository;

    @Autowired
    public UnitOfMeasureServiceImpl(UnitOfMeasureRepository unitOfMeasureRepository, ObjectFactory objectFactory,
                                    ProductRepository productRepository) {
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.productRepository = productRepository;
        this.objectFactory = objectFactory;
    }

    @Override
    public UnitOfMeasure findUnitOfMeasureByName(String name) {
        return unitOfMeasureRepository.findByNameIgnoreCase(name);
    }

    @Override
    public List<String> getAllUnitOfMeasureNames() {
        return unitOfMeasureRepository.findAllByOrderByName()
                .stream()
                .map(UnitOfMeasure::getName)
                .collect(Collectors.toList());
    }

    @Override
    public List<UnitOfMeasureDTO> findAllDTOs() {
        return unitOfMeasureRepository.findAllByOrderByName()
                .stream()
                .filter(Objects::nonNull)
                .map((UnitOfMeasure unitOfMeasure) -> objectFactory.getInstance(UnitOfMeasureDTO.class, unitOfMeasure))
                .collect(Collectors.toList());
    }

    @Override
    public List<UnitOfMeasureDTO> searchName(String name) {
        return unitOfMeasureRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .filter(Objects::nonNull)
                .map((UnitOfMeasure unitOfMeasure) -> objectFactory.getInstance(UnitOfMeasureDTO.class, unitOfMeasure))
                .collect(Collectors.toList());
    }

    @Override
    public UnitOfMeasureDTO createNewDTO() {
        return new UnitOfMeasureDTO();
    }

    @Override
    public UnitOfMeasureDTO saveDTO(UnitOfMeasureDTO entityDTO) {
        if (entityDTO.getId() == null) {
            if (unitOfMeasureRepository.findByNameIgnoreCase(entityDTO.getName()) != null) {
                Notification.show("Данная единица измерения уже внесена в базу");
                return null;
            }
            return new UnitOfMeasureDTO(unitOfMeasureRepository.save(new UnitOfMeasure(entityDTO)));
        } else {
            return new UnitOfMeasureDTO(unitOfMeasureRepository.save(new UnitOfMeasure(entityDTO)));
        }
    }

    @Override
    public void deleteDTO(UnitOfMeasureDTO entityDTO) {
        List<Product> products = productRepository
                .findAllByUnitOfMeasure(unitOfMeasureRepository.findByNameIgnoreCase(entityDTO.getName()));
        if(products.size()>0) {
            Notification.show("Эта единица измерения присвоена " + products.size() + " товарам");
            throw new RuntimeException("Удаление невозможно");
        } else {
            unitOfMeasureRepository.delete(unitOfMeasureRepository.findByNameIgnoreCase(entityDTO.getName()));
        }
    }
}
