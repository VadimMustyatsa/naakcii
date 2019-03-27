package naakcii.by.api.chainproduct;

import naakcii.by.api.service.CrudService;
import naakcii.by.api.util.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
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

	@Override
	@Transactional
	public List<ChainProductDTO> findAllByFilter(LocalDate startDate, LocalDate endDate, String chainName, String productName, String typeName) {
		List<ChainProduct> allByFilter = chainProductRepository.findAllByFilter(startDate, endDate, chainName, productName, typeName);
		return allByFilter
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
	@Transactional
	public List<ChainProductDTO> searchName(String name) {
		return chainProductRepository.findAllByProductNameContainingIgnoreCase(name)
				.stream()
				.filter(Objects::nonNull)
				.map((ChainProduct chainProduct) -> objectFactory.getInstance(ChainProductDTO.class, chainProduct))
				.collect(Collectors.toList());
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
