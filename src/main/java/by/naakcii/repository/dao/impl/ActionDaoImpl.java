package by.naakcii.repository.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import by.naakcii.repository.dao.ActionDao;
import by.naakcii.repository.model.Action;

@Repository
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
public class ActionDaoImpl implements ActionDao {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public Action save(Action action) {
		if(action.getId() == null) {
			em.persist(action);
		} else {
			em.merge(action);
		}
		return action;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Action> findAll() {
		List<Action> actions = em.createNamedQuery("Action.findAll", Action.class).getResultList();
		return actions;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Action> findAllWithDetails() {
		List<Action> actions = em.createNamedQuery("Action.findAllWithDetails", Action.class).getResultList();
		return actions;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Action> findByChainId(Long id) {
		TypedQuery<Action> query = em.createNamedQuery("Action.findByChainId", Action.class);
		query.setParameter("id", id);
		return query.getResultList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Action> findByChainIdWithDetails(Long id) {
		TypedQuery<Action> query = em.createNamedQuery("Action.findByChainIdWithDetails", Action.class);
		query.setParameter("id", id);
		return query.getResultList();
	}

	@Override
	public List<Action> findByChainIdAndProductSubcategory(Long chainId, Long subcategoryId) {
		TypedQuery<Action> query = em.createNamedQuery("Action.findByChainIdAndProductSubcategory", Action.class);
		query.setParameter("id1", chainId);
		query.setParameter("id2", subcategoryId);
		return query.getResultList();
	}

}
