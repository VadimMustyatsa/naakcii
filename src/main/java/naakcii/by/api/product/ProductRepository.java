package naakcii.by.api.product;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import naakcii.by.api.unitofmeasure.UnitOfMeasure;

public interface ProductRepository extends CrudRepository<Product, Long> {
	
	Optional<Product> findByNameAndBarcodeAndUnitOfMeasure(String productName, String productBarcode, UnitOfMeasure unitOfMeasure);
	
    @Modifying
    @Query("update Product product set product.isActive = false where product.id = :productId")
    int softDelete(@Param("productId") Long productId);
}
