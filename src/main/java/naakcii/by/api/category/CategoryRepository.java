package naakcii.by.api.category;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends CrudRepository<Category, Long> {
	
	List<Category> findAllByIsActiveTrueOrderByPriorityAsc();
	List<Category> findAllByOrderByPriority();
	List<Category> findAllByNameContainingIgnoreCase(String name);
	Optional<Category> findByNameIgnoreCase(String name);
	List<Category> findAllByOrderByName();
}
