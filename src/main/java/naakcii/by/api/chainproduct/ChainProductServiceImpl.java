package naakcii.by.api.chainproduct;

import naakcii.by.api.util.ObjectFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ChainProductServiceImpl implements ChainProductService {
	
	private ChainProductRepository chainProductRepository;
	private ObjectFactory objectFactory;
	
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
}
