package naakcii.by.api.repository.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import naakcii.by.api.repository.dao.CategoryDao;
import naakcii.by.api.repository.model.Category;

@Repository
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
public class CategoryDaoImpl implements CategoryDao{

	@PersistenceContext
	private EntityManager em;

	@Transactional(readOnly = true)
	@Override
	public List<Category> findAll() {
		List<Category> categories = em.createNamedQuery("Category.findAll", Category.class).getResultList();
		return categories;
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<Category> findAllWithDetails() {
		List<Category> categories = em.createNamedQuery("Category.findAllWithDetails", Category.class).getResultList();
		return categories;
	}

	@Transactional(readOnly = true)
	@Override
	public List<Category> findAllByIsActiveTrue() {
		List<Category> categories = em.createNamedQuery("Category.findAllByIsActiveTrue", Category.class).getResultList();
		return categories;
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<Category> findAllByIsActiveTrueWithDetails() {
		List<Category> categories = em.createNamedQuery("Category.findAllByIsActiveTrueWithDetails", Category.class).getResultList();
		return categories;
	}
	
	public Category save(Category category) {
		if(category.getId() == null) {
			em.persist(category);
		} else {
			em.merge(category);
		}
		return category;
	}
	
	@Override
	public void softDelete(Long categoryId) {
		Query query = em.createNamedQuery("Category.softDelete");
		query.setParameter("categoryId", categoryId);
		query.executeUpdate();
	}
	
}
