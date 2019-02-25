package naakcii.by.api.chain;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChainRepository extends CrudRepository<Chain, Long> {

    List<Chain> findAllByIsActiveTrueOrderByNameAsc();

    List<Chain> findAllByIsActiveTrueOrderByNameDesc();

    Optional<Chain> findByNameAndSynonym(String chainName, String chainSynonym);

    Optional<Chain> findBySynonym(String chainSynonym);

    int countChainsByIsActiveTrue();

    @Modifying
    @Query("update Chain chain set chain.isActive = false where chain.id = :chainId")
    int softDelete(@Param("chainId") Long chainId);

    @Query("select chain.synonym from Chain chain")
    List<String> getAllSynonyms();
}
