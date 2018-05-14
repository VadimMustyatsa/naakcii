package naakcii.by.api.repository.dao;

import java.util.List;

import naakcii.by.api.repository.model.Category;

public interface CategoryDao extends GenericDao<Category, Long> {
	
	List<Category> findAll();
	List<Category> findAllWithDetails();
	List<Category> findAllByIsActiveTrue();
	List<Category> findAllByIsActiveTrueWithDetails();
	Category save(Category category);
	void softDelete(Long categoryId);

}
