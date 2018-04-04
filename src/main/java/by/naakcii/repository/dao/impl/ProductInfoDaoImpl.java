package by.naakcii.repository.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import by.naakcii.repository.dao.ProductInfoDao;
import by.naakcii.repository.model.ProductInfo;

@Repository
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
public class ProductInfoDaoImpl implements ProductInfoDao {
	
	@PersistenceContext
	private EntityManager em;

	@Transactional(readOnly = true)
	@Override
	public List<ProductInfo> findAll() {
		List<ProductInfo> productInfos = em.createNamedQuery("ProductInfo.findAll", ProductInfo.class).getResultList();
		return productInfos;
	}

	@Transactional(readOnly = true)
	@Override
	public List<ProductInfo> findAllWithDetails() {
		List<ProductInfo> productInfos = em.createNamedQuery("ProductInfo.findAllWithDetails", ProductInfo.class).getResultList();
		return productInfos;
	}

	@Transactional(readOnly = true)
	@Override
	public ProductInfo findById(Long id) {
		TypedQuery<ProductInfo> query = em.createNamedQuery("ProductInfo.findById", ProductInfo.class);
		query.setParameter("id", id);
		return query.getSingleResult();
	}

	@Transactional(readOnly = true)
	@Override
	public ProductInfo findByIdWithDetails(Long id) {
		TypedQuery<ProductInfo> query = em.createNamedQuery("ProductInfo.findByIdWithDetails", ProductInfo.class);
		query.setParameter("id", id);
		return query.getSingleResult();
	}

	@Override
	public ProductInfo save(ProductInfo productInfo) {
		if(productInfo.getId() == null) {
			em.persist(productInfo);
		} else {
			em.merge(productInfo);
		}
		return productInfo;
	}

	@Override
	public void softDelete(ProductInfo productInfo) {
		// TODO Auto-generated method stub
		
	}

}
