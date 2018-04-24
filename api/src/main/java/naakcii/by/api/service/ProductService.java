package naakcii.by.api.service;

import naakcii.by.api.service.modelDTO.ProductDTO;

import java.util.List;

public interface ProductService {

    public List<ProductDTO> getProductsBySubcategoryID(Long id);
}
