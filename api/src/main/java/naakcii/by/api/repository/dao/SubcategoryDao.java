package naakcii.by.api.repository.dao;

import java.util.List;

import naakcii.by.api.repository.model.Subcategory;

public interface SubcategoryDao extends GenericDao<Subcategory, Long> {

	List<Subcategory> findAll();
	List<Subcategory> findAllWithDetails();
	Subcategory findById(Long id);
	Subcategory findByIdWithDetails(Long id);
	Subcategory findByNameAndCategoryNameWithDetails(String subcategoryName, String categoryName);
	List<Subcategory> findByCategoryId(Long id);
	Subcategory save(Subcategory subcategory);
	void softDelete(Subcategory subcategory);
	
}
