package io.campsite.db.dao;

import java.util.List;

import org.springframework.data.mongodb.core.query.Query;

public interface BaseDao<T> {

	void save(T entity);

	boolean remove(T entity);

	List<T> findAll();

	T findById(String id);

	<TResult> List<TResult> findAllDistinct(String distinctField, Query query, Class<TResult> resultClass);

	void cleanCollection();

	void createCollectionIfNotExists();

}