package naakcii.by.api.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Page<Product> fetchProducts(int offset, int limit) {
        int page = offset / limit;
        Pageable pageRequest = PageRequest.of(page, limit);
        Page<Product> items = productRepository.findAll(pageRequest);
        return items;
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
    public List<Product> searchName(String search) {
        return productRepository.findByNameContaining(search);
    }
}
