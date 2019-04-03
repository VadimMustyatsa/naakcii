package naakcii.by.api.product;

import naakcii.by.api.country.Country;
import naakcii.by.api.unitofmeasure.UnitOfMeasure;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {
	
	Optional<Product> findByNameAndBarcodeAndUnitOfMeasure(String productName, String productBarcode, UnitOfMeasure unitOfMeasure);

    List<Product> findAllByOrderByName();
    List<Product> findAllByNameContainingIgnoreCase(String search);

    List<Product> findAllByIsActiveTrueOrderByName();
    List<Product> findAllByIsActiveFalseOrderByName();
    List<Product> findAllByUnitOfMeasure(UnitOfMeasure unitOfMeasure);
    List<Product> findAllByCountryOfOrigin(Country country);
    Product findByNameIgnoreCase(String name);
}
