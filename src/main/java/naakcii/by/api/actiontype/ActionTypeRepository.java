package naakcii.by.api.actiontype;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface ActionTypeRepository extends CrudRepository<ActionType, Long> {
	
	Optional<ActionType> findByName(String name);
}
