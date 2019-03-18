package naakcii.by.api.country;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CountryRepository extends CrudRepository<Country, Long> {

	Optional<Country> findByAlphaCode2IgnoreCase(String alphaCode2);
	Optional<Country> findByAlphaCode3IgnoreCase(String alphaCode3);
	Optional<Country> findByNameIgnoreCase(String name);
	List<Country> findAllByOrderByName();
	List<Country> findAllByNameContainingIgnoreCase(String name);
	Optional<Country> findByNameIgnoreCaseAndAlphaCode2AndAlphaCode3(String name, String alphaCode2, String alphaCode3);
}
