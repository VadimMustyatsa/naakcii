package naakcii.by.api.chain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ChainRepository extends CrudRepository<Chain, Long> {
	
	List<Chain> findAllByIsActiveTrueOrderByNameAsc();
	List<Chain> findAllByIsActiveTrueOrderByNameDesc();
	Optional<Chain> findByName(String chainName);
	Optional<Chain> findBySynonym(String chainSynonym);
	
	@Modifying
	@Query("update Chain chain set chain.isActive = false where chain.id = :chainId")
	int softDelete(@Param("chainId") Long chainId);
	
	@Query("select chain.synonym from Chain chain")
	List<String> getAllSynonyms();
}
