package io.campsite.db.dao;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.QueryMapper;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.lang.Nullable;

public abstract class BaseDaoImpl<T> implements BaseDao<T> {

	private final MongoTemplate mongoTemplate;
	private final Class<T> typeOfDocument;

	@SuppressWarnings("unchecked")
	public BaseDaoImpl(MongoTemplate mongoTemplate) {
		super();
		this.mongoTemplate = mongoTemplate;
		this.typeOfDocument = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
	}

	public String getCollectionName() {
		return mongoTemplate.getCollectionName(typeOfDocument);
	}

	@Override
	public void save(T entity) {
		mongoTemplate.save(entity);
	}

	@Override
	public List<T> findAll() {
		return mongoTemplate.findAll(typeOfDocument);
	}

	@Override
	public <TResult> List<TResult> findAllDistinct(String distinctField, @Nullable Query query,
			Class<TResult> resultClass) {
		QueryMapper mapper = new QueryMapper(mongoTemplate.getConverter());
		Document mappedQuery = mapper.getMappedObject(query.getQueryObject(), Optional.empty());
		List<TResult> result = mongoTemplate.getCollection(getCollectionName())
				.distinct(distinctField, mappedQuery, resultClass).into(new ArrayList<>());
		return result;
	}

}
