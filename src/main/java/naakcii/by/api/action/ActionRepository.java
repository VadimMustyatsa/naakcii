package naakcii.by.api.action;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface ActionRepository extends CrudRepository<Action, Action.Id> {
	
	Optional<Action> findByStartDateAndEndDateAndBasePriceAndDiscountPriceAndTypeIdAndChainIdAndProductId(
			Calendar startDate,
			Calendar endDate,
			BigDecimal basePrice,
			BigDecimal discountPrice,
			Long typeId,
			Long chainId,
			Long productId
	);
	
	List<Action> findByProductIsActiveTrueAndProductSubcategoryIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
    		Long subcategoryId, 
    		Calendar startDateRestriction, 
    		Calendar endDateRestriction
    );
	
	List<Action> findByProductIsActiveTrueAndProductSubcategoryIdInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
    		Set<Long> subcategoryIds, 
    		Calendar startDateRestriction, 
    		Calendar endDateRestriction
    );
    
	List<Action> findByProductIsActiveTrueAndProductSubcategoryIdInAndChainIdInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
    		Set<Long> subcategoryIds, 
    		Set<Long> chainIds, 
    		Calendar startDateRestriction, 
    		Calendar endDateRestriction
    );
    List<Action> findByProductIsActiveTrueAndProductSubcategoryIdInAndChainIdInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
    		Set<Long> subcategoryIds, 
    		Set<Long> chainIds, 
    		Calendar startDateRestriction, 
    		Calendar endDateRestriction,
    		Pageable pageable
    );
}
