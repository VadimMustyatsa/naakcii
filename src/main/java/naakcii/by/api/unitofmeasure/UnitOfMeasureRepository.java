package naakcii.by.api.unitofmeasure;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UnitOfMeasureRepository extends CrudRepository<UnitOfMeasure, Long> {

    Optional<UnitOfMeasure> findByNameIgnoreCase(String unitOfMeasureName);
}
