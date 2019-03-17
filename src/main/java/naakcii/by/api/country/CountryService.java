package naakcii.by.api.country;

import java.util.List;
import java.util.Optional;


public interface CountryService {

    Country findByName(String name);

    List<Country> findAll();

    Optional<Country> findByAlphaCode2(String alphaCode2);

    Optional<Country> findByAlphaCode3(String alphaCode3);
}
