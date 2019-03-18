package naakcii.by.api.product;

import com.vaadin.flow.component.notification.Notification;
import naakcii.by.api.country.CountryService;
import naakcii.by.api.service.CrudService;
import naakcii.by.api.subcategory.SubcategoryService;
import naakcii.by.api.unitofmeasure.UnitOfMeasureService;
import naakcii.by.api.util.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService, CrudService<ProductDTO> {

    private final ProductRepository productRepository;
    private final SubcategoryService subcategoryService;
    private final UnitOfMeasureService unitOfMeasureService;
    private final CountryService countryService;
    private final ObjectFactory objectFactory;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, SubcategoryService subcategoryService,
                              UnitOfMeasureService unitOfMeasureService, CountryService countryService,
                              ObjectFactory objectFactory) {
        this.productRepository = productRepository;
        this.objectFactory = objectFactory;
        this.subcategoryService = subcategoryService;
        this.unitOfMeasureService = unitOfMeasureService;
        this.countryService = countryService;
    }

    @Override
    public void save(Product product) {
        productRepository.save(product);
    }

    @Override
    @Transactional
    public void softDelete(Product product) {
        productRepository.softDelete(product.getId());
    }

    @Override
    public List<ProductDTO> searchName(String search) {
        return productRepository.findAllByNameContainingIgnoreCase(search)
                .stream()
                .filter(Objects::nonNull)
                .map((Product product) -> objectFactory.getInstance(ProductDTO.class, product))
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO createNewDTO() {
        return new ProductDTO();
    }

    @Override
    public ProductDTO saveDTO(ProductDTO entityDTO) {
        Product product = new Product(entityDTO);
        product.setSubcategory(subcategoryService.findByNameAndCategoryName(entityDTO.getSubcategoryName(), entityDTO.getCategoryName()));
        product.setUnitOfMeasure(unitOfMeasureService.findUnitOfMeasureByName(entityDTO.getUnitOfMeasureName()));
        product.setCountryOfOrigin(countryService.findByName(entityDTO.getCountryOfOriginName()));
        Optional<Product> productDB = productRepository.findByNameAndBarcodeAndUnitOfMeasure(product.getName(), product.getBarcode(), product.getUnitOfMeasure());
        if(productDB.isPresent() && entityDTO.getId()==null) {
            Notification.show("Данный товар уже внесен в базу");
            return null;
        } else {
            return new ProductDTO(productRepository.save(product));
        }
    }

    @Override
    public void deleteDTO(ProductDTO entityDTO) {
        Product product = productRepository.findById(entityDTO.getId()).orElse(null);
        if (product == null) {
            throw new EntityNotFoundException();
        } else {
            productRepository.delete(product);
        }
    }

    @Override
    public Product findProduct(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public List<ProductDTO> findAllDTOs() {
        return productRepository.findAllByOrderByName()
                .stream()
                .filter(Objects::nonNull)
                .map((Product product) -> objectFactory.getInstance(ProductDTO.class, product))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> checkIsActive(String filter) {
        if (filter.equals("Активные")) {
            return productRepository.findAllByIsActiveTrue()
                    .stream()
                    .filter(Objects::nonNull)
                    .map((Product product) -> objectFactory.getInstance(ProductDTO.class, product))
                    .collect(Collectors.toList());
        } else {
            return productRepository.findAllByIsActiveFalse()
                    .stream()
                    .filter(Objects::nonNull)
                    .map((Product product) -> objectFactory.getInstance(ProductDTO.class, product))
                    .collect(Collectors.toList());
        }
    }
}
