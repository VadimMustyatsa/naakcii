package naakcii.by.api.category;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoryRepository extends CrudRepository<Category, Long> {
	
	List<Category> findAllByIsActiveTrueOrderByPriorityAsc();

	List<Category> findAllByOrderByPriority();
	List<Category> findAllByNameContainingIgnoreCase(String name);
}
