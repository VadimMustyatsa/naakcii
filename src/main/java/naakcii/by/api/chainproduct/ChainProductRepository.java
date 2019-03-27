package naakcii.by.api.chainproduct;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ChainProductRepository extends CrudRepository<ChainProduct, ChainProduct.Id> {

    List<ChainProduct> findAllByProductIsActiveTrueAndChainIsActiveTrueAndEndDateGreaterThanEqualOrderByChainName(LocalDate endDateRestriction);
    List<ChainProduct> findAllByProductNameContainingIgnoreCase(String name);
    List<ChainProduct> findAllByTypeId(Long chainProductTypeId);

    @Query("select cp from ChainProduct cp "
            + "join cp.chain chainName "
            + "join cp.product productName "
            + "join cp.type typeName "
            + "where ( CAST (:startDate AS date) is null or cp.startDate >= :startDate) "
            + "and ( CAST (:endDate AS date) is null or cp.endDate <= :endDate) "
            + "and ( :chainName is null or chainName.name = :chainName) "
            + "and ( :productName is null or lower(productName.name) LIKE %:productName%) "
            + "and (:typeName is null or typeName.name = :typeName)"
    )
    List<ChainProduct> findAllByFilter(@Param("startDate") LocalDate startDate,
                                       @Param("endDate") LocalDate endDate,
                                       @Param("chainName") String chainName,
                                       @Param("productName") String productName,
                                        @Param("typeName") String typeName);
}
