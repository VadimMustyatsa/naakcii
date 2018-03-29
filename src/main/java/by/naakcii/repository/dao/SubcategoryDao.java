package by.naakcii.repository.dao;

import java.util.List;

import by.naakcii.repository.model.Subcategory;

public interface SubcategoryDao extends GenericDao<Subcategory, Long> {

	List<Subcategory> findAll();
	List<Subcategory> findAllWithDetails();
	Subcategory findById(Long id);
	Subcategory findByIdWithDetails(Long id);
	List<Subcategory> findByCategoryId(Long id);
	Subcategory save(Subcategory subcategory);
	void softDelete(Subcategory subcategory);
	
}
