package naakcii.by.api.category;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends CrudRepository<Category, Long> {
	
	List<Category> findAllByIsActiveTrueOrderByPriorityAsc();
	List<Category> findAllByIsActiveTrueOrderByPriorityDesc();
	Optional<Category> findByName(String categoryName);
	
	@Modifying
	@Query("update Category category set category.isActive = false where category.id = :categoryId")
	int softDelete(@Param("categoryId") Long categoryId);
}
