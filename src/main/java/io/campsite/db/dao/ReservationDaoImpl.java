package io.campsite.db.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import io.campsite.db.model.Reservation;

@Repository
public class ReservationDaoImpl extends BaseDaoImpl<Reservation> implements ReservationDao {

	@Autowired
	public ReservationDaoImpl(MongoTemplate mongoTemplate) {
		super(mongoTemplate);
	}

}
