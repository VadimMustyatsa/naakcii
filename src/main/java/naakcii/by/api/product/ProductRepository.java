package naakcii.by.api.product;

import naakcii.by.api.unitofmeasure.UnitOfMeasure;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {
	
	Optional<Product> findByNameAndBarcodeAndUnitOfMeasure(String productName, String productBarcode, UnitOfMeasure unitOfMeasure);
	
    @Modifying
    @Query("update Product product set product.isActive = false where product.id = :productId")
    int softDelete(@Param("productId") Long productId);

    List<Product> findAllByOrderByName();
    List<Product> findAllByNameContainingIgnoreCase(String search);

    List<Product> findAllByIsActiveTrueOrderByName();
    List<Product> findAllByIsActiveFalseOrderByName();
}
