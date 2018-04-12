package naakcii.by.api.repository.dao;

import java.util.List;

import naakcii.by.api.repository.model.ProductInfo;

public interface ProductInfoDao extends GenericDao<ProductInfo, Long> {
	
	List<ProductInfo> findAll();
	List<ProductInfo> findAllWithDetails();
	ProductInfo findById(Long id);
	ProductInfo findByIdWithDetails(Long id);
	ProductInfo save(ProductInfo productInfo);
	void softDelete(ProductInfo productInfo);

}
