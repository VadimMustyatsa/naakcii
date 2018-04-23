package naakcii.by.api.repository.dao;

import java.util.List;

import naakcii.by.api.repository.model.Category;

public interface CategoryDao extends GenericDao<Category, Long> {
	
	List<Category> findAll();
	List<Category> findAllWithDetails();
	Category findById(Long id);
	Category findByIdWithDetails(Long id);
	Category findByNameWithDetails(String name);
	Category save(Category category);
	void softDelete(Category category);

}
