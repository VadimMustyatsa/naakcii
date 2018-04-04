package by.naakcii.repository.dao;

import java.io.Serializable;
import java.util.List;

public interface GenericDao<T extends Serializable, ID extends Number> {

	List<T> findAll();
	T findById(ID id);
	T save(T o);
	void softDelete(T o);
	
}
