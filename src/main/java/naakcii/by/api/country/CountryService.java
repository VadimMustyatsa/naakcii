package naakcii.by.api.country;

import org.springframework.data.domain.Page;

import java.util.List;

public interface CountryService {

    Country findByName(String name);

    List<Country> findAll();

    List<CountryDTO> findAllDTOs();

    List<CountryDTO> searchName(String name);

    void saveCountryDTO(CountryDTO countryDTO);

    void delete(CountryDTO countryDTO);
}
