package naakcii.by.api.action;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface ActionRepository extends CrudRepository<Action, Action.Id> {
	
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
