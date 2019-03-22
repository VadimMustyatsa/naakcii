package naakcii.by.api.subcategory;

import naakcii.by.api.category.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface SubcategoryRepository extends CrudRepository<Subcategory, Long> {

	Optional<Subcategory> findByNameAndCategoryName(String subcategoryName, String categoryName);
	List<Subcategory> findByCategory(Optional<Category> category);
	List<Subcategory> findAllByOrderByCategoryName();
	List<Subcategory> findAllByNameContainingIgnoreCase(String name);

	Optional<Subcategory> findByNameIgnoreCaseAndCategoryNameIgnoreCase(String subcategoryName, String categoryName);
}
