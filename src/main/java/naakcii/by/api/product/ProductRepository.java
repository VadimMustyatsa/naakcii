package naakcii.by.api.product;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends CrudRepository<Product, Long> {
	
    @Modifying
    @Query("update Product product set product.isActive = false where product.id = :productId")
    int softDelete(@Param("productId") Long productId);
}
