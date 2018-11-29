package naakcii.by.api.action;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ActionRepository extends CrudRepository<Action, Action.Id> {

    @Query("select action from Action action "
            + "left join fetch action.chain chain "
            + "left join fetch action.product product "
            + "where product.subcategory.id = :subcategoryId and :currentDate between action.startDate and action.endDate"
    )
    List<Action> findAllBySubcategoryId(
            @Param("subcategoryId") Long subcategoryId,
            @Param("currentDate") Calendar currentDate
    );

    @Query("select action from Action action "
            + "left join fetch action.chain chain "
            + "left join fetch action.product product "
            + "where product.subcategory.id in :subcategoriesIds and :currentDate between action.startDate and action.endDate"
    )
    List<Action> findAllBySubcategoriesIds(
            @Param("subcategoriesIds") Set<Long> subcategoriesIds,
            @Param("currentDate") Calendar currentDate
    );
    
    /*@Query("select action from Action action "
            + "left join fetch action.chain chain "
            + "left join fetch action.product product "
            + "where product.subcategory.id in :subcategoriesIds and "
            + "chain.id in :chainIds and"
            + ":currentDate between action.startDate and action.endDate"
    )
    List<Action> findAllBySubcategoriesIdsAndChainIds(
            @Param("subcategoriesIds") Set<Long> subcategoriesIds,
            @Param("chainIds") Set<Long> chainIds,
            @Param("currentDate") Calendar currentDate
    );*/
    List<Action> findByProductSubcategoryIdInAndChainIdInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(Set<Long> subcategoriesIds, Set<Long> chainIds, Calendar currentDate1, Calendar currentDate2);

    List<Action> findAllByChainId(Long chainId);
    List<Action> findAllByProductId(Long productId);
}
