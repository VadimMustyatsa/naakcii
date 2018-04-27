package naakcii.by.api.repository.model.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import naakcii.by.api.repository.model.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {

	@Modifying
	@Query("update Product product set product.isActive = false where product.id = :productId")
	void softDelete(@Param("productId") Long productId);
	
}
