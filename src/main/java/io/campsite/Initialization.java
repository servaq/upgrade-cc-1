package io.campsite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import io.campsite.db.dao.ReservationDao;

public class Initialization implements CommandLineRunner {

	@Autowired
	private ReservationDao reservationDao;

	@Override
	public void run(String... args) throws Exception {
		reservationDao.createCollectionIfNotExists();
	}

}
