package naakcii.by.api.product;

import naakcii.by.api.util.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;
    private ObjectFactory objectFactory;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ObjectFactory objectFactory) {
        this.productRepository = productRepository;
        this.objectFactory = objectFactory;
    }

    @Override
    public Page<ProductDTO> fetchProducts(int offset, int limit) {
        int page = offset / limit;
        Pageable pageRequest = PageRequest.of(page, limit, Sort.by("name"));
        Page<Product> items = productRepository.findAll(pageRequest);

        return new PageImpl<ProductDTO>(items.stream()
        .map(product -> new ProductDTO(product))
        .collect(Collectors.toList()));
    }

    @Override
    public int getProductCount() {
        return (int) productRepository.count();
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
        return productRepository.findByNameContainingOrderByName(search)
                .stream()
                .filter(Objects::nonNull)
                .map((Product product) -> objectFactory.getInstance(ProductDTO.class, product))
                .collect(Collectors.toList());
    }

    @Override
    public Product findProduct(Long id) {
        return productRepository.findById(id).orElse(null);
    }
}
