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

import by.naakcii.repository.dao.CategoryDao;
import by.naakcii.repository.model.Category;

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
	public Category findById(Long id) {
		TypedQuery<Category> query = em.createNamedQuery("Category.findById", Category.class);
		query.setParameter("id", id);
		return query.getSingleResult();
	}
	
	@Transactional(readOnly = true)
	@Override
	public Category findByIdWithDetails(Long id) {
		TypedQuery<Category> query = em.createNamedQuery("Category.findByIdWithDetails", Category.class);
		query.setParameter("id", id);
		return query.getSingleResult();
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
	public void softDelete(Category category) {
		Query query = em.createNamedQuery("Category.softDelete");
		query.setParameter("id", category.getId());
		query.executeUpdate();
	}
	
}
