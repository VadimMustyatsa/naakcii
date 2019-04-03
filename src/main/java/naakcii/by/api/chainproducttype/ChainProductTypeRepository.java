package naakcii.by.api.chainproducttype;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ChainProductTypeRepository extends CrudRepository<ChainProductType, Long> {
	
	Optional<ChainProductType> findByNameIgnoreCaseAndSynonymIgnoreCase(String typeName, String typeSynonym);
	List<ChainProductType> findAllByOrderByName();
	List<ChainProductType> findAllByNameContainingIgnoreCase(String name);
	ChainProductType findByNameIgnoreCase(String name);
}
