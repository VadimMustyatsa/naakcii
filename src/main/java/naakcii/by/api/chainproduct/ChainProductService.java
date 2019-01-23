package naakcii.by.api.chainproduct;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Pageable;

public interface ChainProductService {
	
	List<ChainProductDTO> getAllProductsByChainIdsAndSubcategoryIds(Set<Long> chainIds, Set<Long> subcategoryIds, Pageable pageable); 
}
