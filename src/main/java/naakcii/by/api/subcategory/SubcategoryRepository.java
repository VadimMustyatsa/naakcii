package naakcii.by.api.subcategory;

import naakcii.by.api.category.Category;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SubcategoryRepository extends CrudRepository<Subcategory, Long> {
	
	List<Subcategory> findByIsActiveTrueAndCategoryIdOrderByPriorityAsc(Long categoryId);
	List<Subcategory> findByIsActiveTrueAndCategoryIdOrderByPriorityDesc(Long categoryId);
	Optional<Subcategory> findByNameAndCategoryName(String subcategoryName, String categoryName);
	List<Subcategory> findByCategory(Optional<Category> category);
//	Subcategory findByNameAndCategoryName(String subcategoryName);

	@Modifying
	@Query("update Subcategory subcategory set subcategory.isActive = false where subcategory.id = :subcategoryId")
	int softDelete(@Param("subcategoryId") Long subcategoryId);
}
