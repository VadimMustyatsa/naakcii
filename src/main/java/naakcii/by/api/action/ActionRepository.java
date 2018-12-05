package naakcii.by.api.action;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface ActionRepository extends CrudRepository<Action, Action.Id> {
	
	List<Action> findByProductSubcategoryIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
    		Long subcategoryId, 
    		Calendar startDateRestriction, 
    		Calendar endDateRestriction
    );
	
	List<Action> findByProductSubcategoryIdInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
    		Set<Long> subcategoryIds, 
    		Calendar startDateRestriction, 
    		Calendar endDateRestriction
    );
    
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
