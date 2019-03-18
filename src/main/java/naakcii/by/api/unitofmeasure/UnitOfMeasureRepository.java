package naakcii.by.api.unitofmeasure;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UnitOfMeasureRepository extends CrudRepository<UnitOfMeasure, Long>{

	UnitOfMeasure findByNameIgnoreCase(String unitOfMeasureName);

	List<UnitOfMeasure> findAllByOrderByName();
}
