package naakcii.by.api.product;

import java.util.List;
import java.util.Set;

public interface ProductService {
	
	List<ProductDTO> getAllProductsByChainIdsAndSubcategoryIds(Set<Long> chainIds, Set<Long> subcategoryIds); 
}
