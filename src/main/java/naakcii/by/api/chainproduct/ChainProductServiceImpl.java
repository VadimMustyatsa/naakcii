package naakcii.by.api.chainproduct;

import com.vaadin.flow.component.notification.Notification;
import naakcii.by.api.chain.Chain;
import naakcii.by.api.chain.ChainRepository;
import naakcii.by.api.chainproducttype.ChainProductType;
import naakcii.by.api.chainproducttype.ChainProductTypeRepository;
import naakcii.by.api.product.Product;
import naakcii.by.api.product.ProductRepository;
import naakcii.by.api.service.CrudService;
import naakcii.by.api.util.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChainProductServiceImpl implements ChainProductService, CrudService<ChainProductDTO> {
	
	private final ChainProductRepository chainProductRepository;
	private final ChainRepository chainRepository;
	private final ProductRepository productRepository;
	private final ChainProductTypeRepository chainProductTypeRepository;
	private final ObjectFactory objectFactory;
	
	@Autowired
	public ChainProductServiceImpl(ChainProductRepository chainProductRepository, ChainRepository chainRepository,
								   ProductRepository productRepository,
								   ChainProductTypeRepository chainProductTypeRepository, ObjectFactory objectFactory) {
		this.chainProductRepository = chainProductRepository;
		this.chainRepository = chainRepository;
		this.productRepository = productRepository;
		this.chainProductTypeRepository = chainProductTypeRepository;
		this.objectFactory = objectFactory;
	}

	@Override
	@Transactional
	public List<ChainProductDTO> findAllByFilter(LocalDate startDate, LocalDate endDate, String chainName,
												 String productName, String typeName) {
		if (productName!=null) {
			productName = productName.toLowerCase();
		}
		return chainProductRepository.findAllByFilter(startDate, endDate, chainName	, productName, typeName)
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
	@Transactional
	public ChainProductDTO saveDTO(ChainProductDTO entityDTO) {
		Chain chain = chainRepository.findByNameIgnoreCase(entityDTO.getChainName());
		Product product = productRepository.findByNameIgnoreCase(entityDTO.getProductName());
		ChainProductType type = chainProductTypeRepository.findByNameIgnoreCase(entityDTO.getChainProductTypeName());
		ChainProduct chainProduct = new ChainProduct(entityDTO);
		chainProduct.setType(type);
		chainProduct.setProduct(product);
		chainProduct.setChain(chain);
		chainProduct.setId(new ChainProduct.Id(product.getId(), chain.getId()));

		Optional<ChainProduct> chainProductDB = chainProductRepository.findAllByProductIdAndChainId(chainProduct.getId().getProductId(), chainProduct.getId().getChainId());
		if (entityDTO.getId()==null) {
			if(chainProductDB.isPresent()) {
				Notification.show("Данная акция уже внесена в базу");
				return null;
			}
			return new ChainProductDTO(chainProductRepository.save(chainProduct));
		} else {
			chainProductRepository.delete(chainProductRepository.findById(entityDTO.getId()).orElse(null));
			return new ChainProductDTO(chainProductRepository.save(chainProduct));
		}
	}

	@Override
	public void deleteDTO(ChainProductDTO entityDTO) {
		ChainProduct chainProductDB = chainProductRepository.findById(entityDTO.getId()).orElse(null);
		if (chainProductDB==null) {
			throw new EntityNotFoundException();
		} else {
			chainProductRepository.delete(chainProductDB);
		}
	}
}
