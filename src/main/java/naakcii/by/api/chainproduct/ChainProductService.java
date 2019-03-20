package naakcii.by.api.chainproduct;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface ChainProductService {
	
	List<ChainProductDTO> getAllProductsByChainIdsAndSubcategoryIds(Set<Long> chainIds, Set<Long> subcategoryIds, Pageable pageable); 
}
