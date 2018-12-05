package naakcii.by.api.product;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Pageable;

public interface ProductService {
	
	List<ProductDTO> getAllProductsByChainIdsAndSubcategoryIds(Set<Long> chainIds, Set<Long> subcategoryIds, Pageable pageable); 
}
