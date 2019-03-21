package naakcii.by.api.country;

import java.util.List;


public interface CountryService {

    Country findByName(String name);

    List<String> getAllCountryNames();
}
