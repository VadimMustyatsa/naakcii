package naakcii.by.api.repository.dao;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

import naakcii.by.api.repository.model.Action;

public interface ActionDao {
	
	List<Action> findAllBySubcategoryId(Long subcategoryId, Calendar currentDate);
	List<Action> findAllBySubcategoriesIds(Set<Long> subcategoriesIds, Calendar currentDate);
	Action save(Action action);

}
