package naakcii.by.api.chainproduct;

import naakcii.by.api.service.CrudService;
import naakcii.by.api.util.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChainProductServiceImpl implements ChainProductService, CrudService<ChainProductDTO> {
	
	private final ChainProductRepository chainProductRepository;
	private final ObjectFactory objectFactory;
	
	@Autowired
	public ChainProductServiceImpl(ChainProductRepository chainProductRepository, ObjectFactory objectFactory) {
		this.chainProductRepository = chainProductRepository;
		this.objectFactory = objectFactory;
	}
	
	private Calendar getCurrentDate() {
		Calendar currentDate = Calendar.getInstance();
		return new GregorianCalendar(currentDate.get(Calendar.YEAR), 
									 currentDate.get(Calendar.MONTH), 
									 currentDate.get(Calendar.DAY_OF_MONTH)
		);
	}

	@Override
	public List<ChainProductDTO> getAllProductsByChainIdsAndSubcategoryIds(Set<Long> subcategoryIds, Set<Long> chainIds, Pageable pageable) {
		return chainProductRepository.findByProductIsActiveTrueAndProductSubcategoryIdInAndChainIdInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(subcategoryIds, chainIds, getCurrentDate(), getCurrentDate(), pageable)
				.stream()
				.filter(Objects::nonNull)
				.map((ChainProduct action) -> objectFactory.getInstance(ChainProductDTO.class, action))
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public List<ChainProductDTO> findAllByFilter(LocalDate date) {
		return chainProductRepository.findAllByStartDateGreaterThanEqual(date)
				.stream()
				.filter(Objects::nonNull)
				.map((ChainProduct action) -> objectFactory.getInstance(ChainProductDTO.class, action))
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public List<ChainProductDTO> findAllDTOs() {
		return chainProductRepository.findAllByProductIsActiveTrueAndChainIsActiveTrueAndEndDateGreaterThanEqualOrderByChainName(LocalDate.now())
				.stream()
				.filter(Objects::nonNull)
				.map((ChainProduct chainProduct) -> objectFactory.getInstance(ChainProductDTO.class, chainProduct))
				.collect(Collectors.toList());
	}

	@Override
	public List<ChainProductDTO> searchName(String name) {
		return null;
	}

	@Override
	public ChainProductDTO createNewDTO() {
		return new ChainProductDTO();
	}

	@Override
	public ChainProductDTO saveDTO(ChainProductDTO entityDTO) {
		return null;
	}

	@Override
	public void deleteDTO(ChainProductDTO entityDTO) {

	}
}
