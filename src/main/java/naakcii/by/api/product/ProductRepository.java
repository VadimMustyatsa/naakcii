package naakcii.by.api.product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import naakcii.by.api.unitofmeasure.UnitOfMeasure;

public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {
	
	Optional<Product> findByNameAndBarcodeAndUnitOfMeasure(String productName, String productBarcode, UnitOfMeasure unitOfMeasure);
	
    @Modifying
    @Query("update Product product set product.isActive = false where product.id = :productId")
    int softDelete(@Param("productId") Long productId);

    List<Product> findByNameContaining(String search);
}
