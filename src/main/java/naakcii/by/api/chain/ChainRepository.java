package naakcii.by.api.chain;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ChainRepository extends CrudRepository<Chain, Long> {

    List<Chain> findAllByOrderByName();
    Optional<Chain> findBySynonymIgnoreCase(String synonym);
    List<Chain> findAllByNameContainingIgnoreCase(String name);
    List<Chain> findAllByIsActiveTrueOrderByName();
    List<Chain> findAllByIsActiveFalseOrderByName();
    Chain findByNameIgnoreCase(String name);
}
