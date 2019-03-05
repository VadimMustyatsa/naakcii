package naakcii.by.api.unitofmeasure;

import java.util.Optional;

public interface UnitOfMeasureService {

    Optional<UnitOfMeasure> findUnitOfMeasureByName(String name);

}
