package naakcii.by.api.repository.dao;

import java.util.List;

import naakcii.by.api.repository.model.Product;

public interface ProductDao extends GenericDao<Product, Long> {
	
	List<Product> findAll();
	List<Product> findAllWithDetails();
	Product findById(Long id);
	Product findByIdWithDetails(Long id);
	List<Product> findBySubcategoryId(Long id);
	Product save(Product product);
	void softDelete(Product product);

}
