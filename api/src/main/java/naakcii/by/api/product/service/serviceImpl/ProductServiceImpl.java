package naakcii.by.api.product.service.serviceImpl;

import naakcii.by.api.action.repository.ActionRepository;
import naakcii.by.api.action.repository.model.Action;
import naakcii.by.api.product.repository.ProductRepository;
import naakcii.by.api.product.repository.model.Product;
import naakcii.by.api.product.service.ProductService;
import naakcii.by.api.product.service.modelDTO.ProductDTO;
import naakcii.by.api.product.service.util.ProductConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductConverter productConverter = new ProductConverter();

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ActionRepository actionRepository;

    @Override
    public List<ProductDTO> getProductsByChainIdAndSubcategoryId(Long subcategoryId) {
        List<Product> productList = productRepository.getBySubcategoryId(subcategoryId);
        List<ProductDTO> productDTOList = new ArrayList<>();
        for (Product product : productList) {
            List<Action> actionList = actionRepository.findAllByProductId(product.getId());
            for (Action action : actionList) {
                productDTOList.add(productConverter.convert(product, action));
            }
        }
        Collections.sort(productDTOList, Comparator.comparing(ProductDTO::getDiscountPrice));
        return productDTOList;
    }
}
