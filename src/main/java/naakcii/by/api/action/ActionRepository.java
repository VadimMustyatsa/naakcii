package naakcii.by.api.action;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface ActionRepository extends CrudRepository<Action, Action.Id> {
	/*
    @Query("select action from Action action "
            + "left join fetch action.chain chain "
            + "left join fetch action.product product "
            + "where product.subcategory.id = :subcategoryId and :currentDate between action.startDate and action.endDate"
    )
    List<Action> findAllBySubcategoryId(
            @Param("subcategoryId") Long subcategoryId,
            @Param("currentDate") Calendar currentDate
    );
    */
	List<Action> findByProductSubcategoryIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
    		Long subcategoryId, 
    		Calendar startDateRestriction, 
    		Calendar endDateRestriction
    );
	/*
    @Query("select action from Action action "
            + "left join fetch action.chain chain "
            + "left join fetch action.product product "
            + "where product.subcategory.id in :subcategoryIds and :currentDate between action.startDate and action.endDate"
    )
    List<Action> findAllBySubcategoriesIds(
            @Param("subcategoryIds") Set<Long> subcategoriesIds,
            @Param("currentDate") Calendar currentDate
    );
    */
	List<Action> findByProductSubcategoryIdInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
    		Set<Long> subcategoryIds, 
    		Calendar startDateRestriction, 
    		Calendar endDateRestriction
    );
    /*
    @Query("select action from Action action "
            + "left join fetch action.chain chain "
            + "left join fetch action.product product "
            + "where product.subcategory.id in :subcategoryIds and "
            + "chain.id in :chainIds and"
            + ":currentDate between action.startDate and action.endDate"
    )
    List<Action> findAllBySubcategoriesIdsAndChainIds(
            @Param("subcategoryIds") Set<Long> subcategoriesIds,
            @Param("chainIds") Set<Long> chainIds,
            @Param("currentDate") Calendar currentDate
    );
    */
	List<Action> findByProductSubcategoryIdInAndChainIdInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
    		Set<Long> subcategoryIds, 
    		Set<Long> chainIds, 
    		Calendar startDateRestriction, 
    		Calendar endDateRestriction
    );
    List<Action> findByProductSubcategoryIdInAndChainIdInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
    		Set<Long> subcategoryIds, 
    		Set<Long> chainIds, 
    		Calendar startDateRestriction, 
    		Calendar endDateRestriction,
    		Pageable pageable
    );
    List<Action> findAllByChainId(Long chainId);
    List<Action> findAllByProductId(Long productId);
}
