package by.naakcii.repository.dao;

import java.util.List;

import by.naakcii.repository.model.Product;

public interface ProductDao extends GenericDao<Product, Long> {
	
	List<Product> findAll();
	//List<Subcategory> findAllWithDetails();
	Product findById(Long id);
	//Subcategory findByIdWithDetails(Long id);
	Product save(Product product);
	void softDelete(Product product);

}
