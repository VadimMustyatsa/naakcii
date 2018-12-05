package naakcii.by.api.product.service.util;

import naakcii.by.api.action.repository.model.Action;
import naakcii.by.api.product.repository.model.Product;
import naakcii.by.api.product.service.modelDTO.ProductDTO;
import org.springframework.stereotype.Component;

@Component
public class ProductConverter {

    public ProductDTO convert(Product product, Action action) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setChainId(action.getChain().getId());
        productDTO.setSubcategoryId(product.getSubcategory().getId());
        productDTO.setName(product.getName());
        productDTO.setPrice(action.getPrice());
        productDTO.setDiscount(action.getDiscount());
        productDTO.setDiscountPrice(action.getDiscountPrice());
        productDTO.setPicture(product.getPicture());
        productDTO.setMeasure(product.getMeasure());
        productDTO.setType(action.getType());
        productDTO.setStartDate(action.getStartDate());
        productDTO.setEndDate(action.getEndDate());
        return productDTO;
    }

}
