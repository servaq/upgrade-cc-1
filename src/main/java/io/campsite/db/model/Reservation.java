package io.campsite.db.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class Reservation {

	@Id
	private String id;

	private String fullname;

	private String email;

	@Indexed(unique = true)
	private List<String> dates;

	protected Reservation() {
	}

	public Reservation(String fullname, String email, List<String> dates) {
		this.fullname = fullname;
		this.email = email;
		this.dates = dates;
	}

}
