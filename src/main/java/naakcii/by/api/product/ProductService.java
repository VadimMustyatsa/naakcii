package naakcii.by.api.product;

import java.util.List;

public interface ProductService {

    List<ProductDTO> searchName(String search);

    List<ProductDTO> checkIsActive(String filter);
}
