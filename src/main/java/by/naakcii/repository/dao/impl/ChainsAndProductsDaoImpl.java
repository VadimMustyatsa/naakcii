package by.naakcii.repository.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import by.naakcii.repository.dao.ChainsAndProductsDao;
import by.naakcii.repository.model.ChainsAndProducts;

@Repository
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
public class ChainsAndProductsDaoImpl implements ChainsAndProductsDao {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public ChainsAndProducts save(ChainsAndProducts chainsAndProducts) {
		if(chainsAndProducts.getId() == null) {
			em.persist(chainsAndProducts);
			System.out.println("persist");
		} else {
			em.merge(chainsAndProducts);
			System.out.println("merge");
		}
		return chainsAndProducts;
	}

}
