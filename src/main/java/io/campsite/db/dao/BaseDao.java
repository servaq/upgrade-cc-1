package io.campsite.db.dao;

import java.util.List;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.lang.Nullable;

public interface BaseDao<T> {

	void save(T entity);

	List<T> findAll();

	<TResult> List<TResult> findAllDistinct(String distinctField, Query query, Class<TResult> resultClass);

}