package naakcii.by.api.service.util;

import naakcii.by.api.repository.model.Action;
import naakcii.by.api.repository.model.Product;
import naakcii.by.api.service.modelDTO.ProductDTO;

public class ProductConverter {

    public ProductConverter() {
    }

    public ProductDTO convert(Product product, Action action) {
        ProductDTO productDTO = new ProductDTO();
        /*productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getProductInfo().getDescription());
        productDTO.setIcon(product.getIcon());
        productDTO.setSubcategoryId(product.getSubcategory().getId());
        productDTO.setChainId(action.getChain().getId());
        productDTO.setQuantity(product.getProductInfo().getQuantity());
        productDTO.setMeasure(product.getProductInfo().getMeasure());
        productDTO.setPrice(action.getPrice());
        productDTO.setDiscount(action.getDiscount());
        productDTO.setDiscountPrice(action.getDiscountPrice());*/
        return productDTO;
    }
}
