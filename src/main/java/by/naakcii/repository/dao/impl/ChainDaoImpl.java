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

import by.naakcii.repository.dao.ChainDao;
import by.naakcii.repository.model.Chain;

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
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Chain save(Chain chain) {
		if(chain.getId() == null) {
			em.persist(chain);
			System.out.println("persist");
		} else {
			em.merge(chain);
			System.out.println("merge");
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
