package naakcii.by.api.chainproducttype;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ChainProductTypeRepository extends CrudRepository<ChainProductType, Long> {
	
	Optional<ChainProductType> findByNameAndSynonym(String typeName, String typeSynonym);
	Optional<ChainProductType> findBySynonym(String typeSynonym);
}
