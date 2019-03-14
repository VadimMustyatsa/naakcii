package naakcii.by.api.country;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CountryRepository extends CrudRepository<Country, Long> {
	
	Optional<Country> findByAlphaCode2(String alphaCode2);
	Optional<Country> findByAlphaCode2AndAlphaCode3(String alphaCode2, String alphaCode3);
	Optional<Country> findByName(String name);
	List<Country> findAllByOrderByName();
	List<Country> findAllByNameContainingIgnoreCase(String name);
}
