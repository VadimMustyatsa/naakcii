package naakcii.by.api.product;

import naakcii.by.api.unitofmeasure.UnitOfMeasure;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product, Long> {

    Optional<Product> findByNameAndBarcodeAndUnitOfMeasure(String productName, String productBarcode, UnitOfMeasure unitOfMeasure);

    int countProductsByIsActiveTrue();

    @Modifying
    @Query("update Product product set product.isActive = false where product.id = :productId")
    int softDelete(@Param("productId") Long productId);
}
