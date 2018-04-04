package by.naakcii.repository.dao;

import java.util.List;

import by.naakcii.repository.model.Chain;

public interface ChainDao extends GenericDao<Chain, Long> {
	
	List<Chain> findAll();
	List<Chain> findAllWithDetails();
	Chain findById(Long id);
	Chain findByIdWithDetails(Long id);
	Chain save(Chain chain);
	void softDelete(Chain chain);

}
