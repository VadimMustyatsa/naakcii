package naakcii.by.api.service.ServiceImpl;

import naakcii.by.api.repository.dao.ActionDao;
import naakcii.by.api.repository.dao.ProductDao;
import naakcii.by.api.repository.model.Action;
import naakcii.by.api.repository.model.Product;
import naakcii.by.api.service.ProductService;
import naakcii.by.api.service.modelDTO.ProductDTO;
import naakcii.by.api.service.util.ProductConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductConverter converter = new ProductConverter();

    @Autowired
    private ProductDao repository;

    @Autowired
    private ActionDao actionDao;

    @Override
    public List<ProductDTO> getProductsBySubcategoryID(Long id) {
        List<Product> productList = repository.findBySubcategoryId(id);
        List<ProductDTO> productDTOList = new ArrayList<ProductDTO>();
        for (Product product : productList) {
            List<Action> actionList = actionDao.findByProductId(product.getId());
            for (Action action : actionList) {
                productDTOList.add(converter.convert(product, action));
            }
        }
        Collections.sort(productDTOList, Comparator.comparing(ProductDTO::getName));
        return productDTOList;
    }
}
