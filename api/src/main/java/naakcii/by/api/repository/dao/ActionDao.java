package naakcii.by.api.repository.dao;

import java.util.List;

import naakcii.by.api.repository.model.Action;

public interface ActionDao {
	
	List<Action> findAll();
	List<Action> findAllWithDetails();
	List<Action> findByChainId(Long id);
	List<Action> findByChainIdWithDetails(Long id);
	List<Action> findByChainIdAndProductSubcategory(Long chainId, Long subcategoryId);
	Action save(Action action);

}
