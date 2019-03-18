package naakcii.by.api.product;

import java.util.List;

public interface ProductService {

    void save(Product product);

    void softDelete(Product product);

    List<ProductDTO> searchName(String search);

    Product findProduct(Long id);

    List<ProductDTO> findAllDTOs();

    List<ProductDTO> checkIsActive(String filter);
}
