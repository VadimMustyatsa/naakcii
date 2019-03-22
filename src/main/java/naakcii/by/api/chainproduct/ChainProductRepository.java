package naakcii.by.api.chainproduct;

import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface ChainProductRepository extends CrudRepository<ChainProduct, ChainProduct.Id> {

    List<ChainProduct> findAllByProductIsActiveTrueAndChainIsActiveTrueAndEndDateGreaterThanEqualOrderByChainName(LocalDate endDateRestriction);
    List<ChainProduct> findAllByStartDateGreaterThanEqual(LocalDate startDateRestriction);
    List<ChainProduct> findAllByProductNameContainingIgnoreCase(String name);
    List<ChainProduct> findAllByTypeId(Long chainProductTypeId);
}
