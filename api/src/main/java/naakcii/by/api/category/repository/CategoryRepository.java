package naakcii.by.api.category.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import naakcii.by.api.category.repository.model.Category;

public interface CategoryRepository extends CrudRepository<Category, Long> {
	
	List<Category> findAll();
	List<Category> findAllByIsActiveTrue();
	List<Category> findAllByIsActiveTrueOrderByPriorityAsc();
	@Modifying
	@Query("update Category category set category.isActive = false where category.id = :categoryId")
	void softDelete(@Param("categoryId") Long categoryId);
	
}
