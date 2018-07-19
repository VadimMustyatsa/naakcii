package naakcii.by.api.chain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import naakcii.by.api.chain.repository.model.Chain;

public interface ChainRepository extends CrudRepository<Chain, Long> {
	List<Chain> findAllByOrderByNameAsc();
	List<Chain> findAllByOrderByNameDesc();
	List<Chain> findAllByIsActiveTrueOrderByNameAsc();
	List<Chain> findAllByIsActiveTrueOrderByNameDesc();
	Chain findByName(String chainName);
	@Modifying
	@Query("update Chain chain set chain.isActive = false where chain.id = :chainId")
	void softDelete(@Param("chainId") Long chainId);
	
}
