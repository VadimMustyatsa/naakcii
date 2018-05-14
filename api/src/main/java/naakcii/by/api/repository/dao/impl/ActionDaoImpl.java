package naakcii.by.api.repository.dao.impl;

import naakcii.by.api.repository.dao.ActionDao;
import naakcii.by.api.repository.model.Action;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

@Repository
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
public class ActionDaoImpl implements ActionDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Action save(Action action) {
        if (action.getId() == null) {
            em.persist(action);
        } else {
            em.merge(action);
        }
        return action;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Action> findAllBySubcategoryId(Long subcategoryId, Calendar currentDate) {
    	TypedQuery<Action> actions = em.createNamedQuery("Action.findAllBySubcategoryId", Action.class);
    	actions.setParameter("subcategoryId", subcategoryId);
    	actions.setParameter("currentDate", currentDate);
        return actions.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Action> findAllBySubcategoriesIds(Set<Long> subcategoriesIds, Calendar currentDate) {
    	TypedQuery<Action> actions = em.createQuery("select ac from Action ac "
    			+ "left join fetch ac.chain ch "
    			+ "left join fetch ac.product p "
    			+ "where p.subcategory.id in :subcategoriesIds "
    			+ "and :currentDate between ac.startDate and ac.endDate", 
    	Action.class);
    	actions.setParameter("subcategoriesIds", subcategoriesIds);
    	actions.setParameter("currentDate", currentDate);
        return actions.getResultList();
    }

}
