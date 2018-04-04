package by.naakcii.repository.dao;

import java.util.List;

import by.naakcii.repository.model.Category;

public interface CategoryDao extends GenericDao<Category, Long> {
	
	List<Category> findAll();
	List<Category> findAllWithDetails();
	Category findById(Long id);
	Category findByIdWithDetails(Long id);
	Category save(Category category);
	void softDelete(Category category);

}
