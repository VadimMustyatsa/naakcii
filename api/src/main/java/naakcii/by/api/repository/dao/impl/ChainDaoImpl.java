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

import naakcii.by.api.repository.dao.ChainDao;
import naakcii.by.api.repository.model.Chain;

@Repository
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
public class ChainDaoImpl implements ChainDao {
	
	@PersistenceContext
	private EntityManager em;

	@Transactional(readOnly = true)
	@Override
	public List<Chain> findAll() {
		List<Chain> chains = em.createNamedQuery("Chain.findAll", Chain.class).getResultList();
		return chains;
	}

	@Transactional(readOnly = true)
	@Override
	public List<Chain> findAllWithDetails() {
		List<Chain> chains = em.createNamedQuery("Chain.findAllWithDetails", Chain.class).getResultList();
		return chains;
	}

	@Transactional(readOnly = true)
	@Override
	public Chain findById(Long id) {
		TypedQuery<Chain> query = em.createNamedQuery("Chain.findById", Chain.class);
		query.setParameter("id", id);
		return query.getSingleResult();
	}

	@Transactional(readOnly = true)
	@Override
	public Chain findByIdWithDetails(Long id) {
		TypedQuery<Chain> query = em.createNamedQuery("Chain.findByIdWithDetails", Chain.class);
		query.setParameter("id", id);
		return query.getSingleResult();
	}

	@Override
	public Chain save(Chain chain) {
		if(chain.getId() == null) {
			em.persist(chain);
		} else {
			em.merge(chain);
		}
		return chain;
	}

	@Override
	public void softDelete(Chain chain) {
		Query query = em.createNamedQuery("Chain.softDelete");
		query.setParameter("id", chain.getId());
		query.executeUpdate();
	}

}
