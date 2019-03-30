package naakcii.by.api.subcategory;

import naakcii.by.api.category.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SubcategoryRepository extends CrudRepository<Subcategory, Long> {

	Optional<Subcategory> findByNameAndCategoryName(String subcategoryName, String categoryName);
	List<Subcategory> findByCategory(Optional<Category> category);
	List<Subcategory> findAllByOrderByName();
	List<Subcategory> findAllByNameContainingIgnoreCase(String name);
	List<Subcategory> findAllByIsActiveTrueOrderByName();
	List<Subcategory> findAllByIsActiveFalseOrderByName();
	Optional<Subcategory> findByNameIgnoreCaseAndCategoryNameIgnoreCase(String subcategoryName, String categoryName);
	List<Subcategory> findAllByCategoryName(String categoryName);

	@Query("select sub from Subcategory sub "
			+ "join sub.category categoryName "
			+ "where ( :categoryName is null or categoryName.name = :categoryName) "
			+ "and ( :subcategoryName is null or lower(sub.name) LIKE %:subcategoryName%) "
			+ "and sub.isActive = :isActive"
	)
	List<Subcategory> findAllByFilter(@Param("subcategoryName") String subcategoryName,
									   @Param("isActive") boolean isActive,
									   @Param("categoryName") String categoryName);
}
