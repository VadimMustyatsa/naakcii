package naakcii.by.api.unitofmeasure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {

    private UnitOfMeasureRepository unitOfMeasureRepository;

    @Autowired
    public UnitOfMeasureServiceImpl(UnitOfMeasureRepository unitOfMeasureRepository) {
        this.unitOfMeasureRepository = unitOfMeasureRepository;
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
}
