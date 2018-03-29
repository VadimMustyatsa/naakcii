package by.naakcii.repository.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import by.naakcii.repository.dao.ProductDao;
import by.naakcii.repository.model.Chain;
import by.naakcii.repository.model.Product;

@Repository
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
public class ProductDaoImpl implements ProductDao {

	@PersistenceContext
	private EntityManager em;
	
	@Transactional(readOnly = true)
	@Override
	public List<Product> findAll() {
		List<Product> products = em.createNamedQuery("Product.findAll", Product.class).getResultList();
		return products;
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<Product> findAllWithDetails() {
		List<Product> products = em.createNamedQuery("Product.findAllWithDetails", Product.class).getResultList();
		return products;
	}

	@Transactional(readOnly = true)
	@Override
	public Product findById(Long id) {
		TypedQuery<Product> query = em.createNamedQuery("Product.findById", Product.class);
		query.setParameter("id", id);
		return query.getSingleResult();
	}
	
	@Transactional(readOnly = true)
	@Override
	public Product findByIdWithDetails(Long id) {
		TypedQuery<Product> query = em.createNamedQuery("Product.findByIdWithDetails", Product.class);
		query.setParameter("id", id);
		return query.getSingleResult();
	}

	@Override
	public Product save(Product product) {
		if(product.getId() == null) {
			em.persist(product);
			System.out.println("persist");
		} else {
			em.merge(product);
			System.out.println("merge");
		}
		return product;
	}

	@Override
	public void softDelete(Product product) {
		Query query = em.createNamedQuery("Product.softDelete");
		query.setParameter("id", product.getId());
		query.executeUpdate();
	}

	@Override
	public List<Product> findBySubcategoryId(Long id) {
		TypedQuery<Product> query = em.createNamedQuery("Product.findBySubcategoryId", Product.class);
		query.setParameter("id", id);
		return query.getResultList();
	}

	@Override
	public List<Product> findBySubcategoryIdAndChainId(Long id1, Chain id2) {
		TypedQuery<Product> query = em.createNamedQuery("Product.findBySubcategoryIdAndChainId", Product.class);
		query.setParameter("id1", id1);
		query.setParameter("id2", id2);
		return query.getResultList();
	}

}
