package naakcii.by.api.country;

import naakcii.by.api.service.CrudService;
import naakcii.by.api.util.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CountryServiceImpl implements CountryService, CrudService<CountryDTO> {

    private final CountryRepository countryRepository;
    private final ObjectFactory objectFactory;

    @Autowired
    public CountryServiceImpl(CountryRepository countryRepository, ObjectFactory objectFactory) {
        this.countryRepository = countryRepository;
        this.objectFactory = objectFactory;
    }

    @Override
    public Country findByName(String name) {
        return countryRepository.findByName(name).orElse(null);
    }

    @Override
    public List<Country> findAll() {
        return countryRepository.findAllByOrderByName();
    }

    @Override
    public List<CountryDTO> findAllDTOs() {
        return countryRepository.findAllByOrderByName()
                .stream()
                .filter(Objects::nonNull)
                .map((Country country) -> objectFactory.getInstance(CountryDTO.class, country))
                .collect(Collectors.toList());
    }

    @Override
    public List<CountryDTO> searchName(String name) {
        return countryRepository.findAllByNameContainingIgnoreCase(name)
                .stream()
                .filter(Objects::nonNull)
                .map((Country country) -> objectFactory.getInstance(CountryDTO.class, country))
                .collect(Collectors.toList());
    }

    @Override
    public CountryDTO createNewDTO() {
        return new CountryDTO();
    }

    @Override
    public void saveDTO(CountryDTO entityDTO) {
        countryRepository.save(new Country(entityDTO));
    }

    @Override
    public void deleteDTO(CountryDTO entityDTO) {
        Country country = countryRepository.findById(entityDTO.getId()).orElse(null);
        if(country == null) {
            throw new EntityNotFoundException();
        }
        countryRepository.delete(country);
    }
}
