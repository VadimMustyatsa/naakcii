package by.naakcii.repository.dao;

import java.util.List;

import by.naakcii.repository.model.Chain;
import by.naakcii.repository.model.Product;

public interface ProductDao extends GenericDao<Product, Long> {
	
	List<Product> findAll();
	List<Product> findAllWithDetails();
	Product findById(Long id);
	Product findByIdWithDetails(Long id);
	List<Product> findBySubcategoryId(Long id);
	List<Product> findBySubcategoryIdAndChainId(Long id1, Chain id2);
	Product save(Product product);
	void softDelete(Product product);

}
