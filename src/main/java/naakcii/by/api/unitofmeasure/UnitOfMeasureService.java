package naakcii.by.api.unitofmeasure;

import java.util.List;

public interface UnitOfMeasureService {

    UnitOfMeasure findUnitOfMeasureByName(String name);

    List<String> getAllUnitOfMeasureNames();

}
