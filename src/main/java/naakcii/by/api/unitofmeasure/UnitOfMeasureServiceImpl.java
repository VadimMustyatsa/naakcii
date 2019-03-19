package naakcii.by.api.unitofmeasure;

import com.vaadin.flow.component.notification.Notification;
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

    @Autowired
    public UnitOfMeasureServiceImpl(UnitOfMeasureRepository unitOfMeasureRepository, ObjectFactory objectFactory) {
        this.unitOfMeasureRepository = unitOfMeasureRepository;
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

    }
}
