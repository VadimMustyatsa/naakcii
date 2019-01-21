package naakcii.by.api.actionproduct;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Pageable;

public interface ActionProductService {
	
	List<ActionProductDTO> getAllProductsByChainIdsAndSubcategoryIds(Set<Long> chainIds, Set<Long> subcategoryIds, Pageable pageable); 
}
