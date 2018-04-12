package naakcii.by.api.repository.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import naakcii.by.api.repository.dao.SubcategoryDao;
import naakcii.by.api.repository.model.Subcategory;

@Repository
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
public class SubcategoryDaoImpl implements SubcategoryDao {
	
	@PersistenceContext
	private EntityManager em;
	
	@Transactional(readOnly = true)
	@Override
	public List<Subcategory> findAll() {
		List<Subcategory> subcategories = em.createNamedQuery("Subcategory.findAll", Subcategory.class).getResultList();
		return subcategories;
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<Subcategory> findAllWithDetails() {
		List<Subcategory> subcategories = em.createNamedQuery("Subcategory.findAllWithDetails", Subcategory.class).getResultList();
		return subcategories;
	}

	@Transactional(readOnly = true)
	@Override
	public Subcategory findById(Long id) {
		TypedQuery<Subcategory> query = em.createNamedQuery("Subcategory.findById", Subcategory.class);
		query.setParameter("id", id);
		return query.getSingleResult();
	}
	
	@Transactional(readOnly = true)
	@Override
	public Subcategory findByIdWithDetails(Long id) {
		TypedQuery<Subcategory> query = em.createNamedQuery("Subcategory.findByIdWithDetails", Subcategory.class);
		query.setParameter("id", id);
		return query.getSingleResult();
	}
	
	public Subcategory save(Subcategory subcategory) {
		if(subcategory.getId() == null) {
			em.persist(subcategory);
		} else {
			em.merge(subcategory);
		}
		return subcategory;
	}
	
	@Override
	public void softDelete(Subcategory subcategory) {
		Query query = em.createNamedQuery("Subcategory.softDelete");
		query.setParameter("id", subcategory.getId());
		query.executeUpdate();
	}

	@Override
	public List<Subcategory> findByCategoryId(Long id) {
		TypedQuery<Subcategory> query = em.createNamedQuery("Subcategory.findByCategoryId", Subcategory.class);
		query.setParameter("id", id);
		return query.getResultList();
	}

	@Transactional(readOnly = true)
	@Override
	public Subcategory findByNameAndCategoryNameWithDetails(String subcategoryName, String categoryName) {
		TypedQuery<Subcategory> query = em.createNamedQuery("Subcategory.findByNameAndCategoryNameWithDetails", Subcategory.class);
		query.setParameter("subcategoryName", subcategoryName);
		query.setParameter("categoryName", categoryName);
		return query.getSingleResult();
	}

}
