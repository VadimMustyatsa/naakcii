package naakcii.by.api.unitofmeasure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
}
