package naakcii.by.api.product;

import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    Page<ProductDTO> fetchProducts(int offset, int limit);

    int getProductCount();

    void save(Product product);

    void softDelete(Product product);

    List<ProductDTO> searchName(String search);

    Product findProduct(Long id);
}
