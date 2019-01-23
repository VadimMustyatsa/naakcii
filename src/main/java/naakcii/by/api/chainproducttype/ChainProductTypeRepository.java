package naakcii.by.api.chainproducttype;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface ChainProductTypeRepository extends CrudRepository<ChainProductType, Long> {
	
	Optional<ChainProductType> findByName(String name);
}
