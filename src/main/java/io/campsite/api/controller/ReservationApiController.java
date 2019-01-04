package io.campsite.api.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.campsite.api.ReservationApi;
import io.campsite.api.error.ApiErrorDetails;
import io.campsite.api.error.ValidationException;
import io.campsite.api.model.ReservationDto;
import io.swagger.annotations.ApiParam;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2019-01-04T15:28:04.296Z")

@Controller
public class ReservationApiController implements ReservationApi {

	private static final Logger log = LoggerFactory.getLogger(ReservationApiController.class);

	private final ObjectMapper objectMapper;

	private final HttpServletRequest request;

	@org.springframework.beans.factory.annotation.Autowired
	public ReservationApiController(ObjectMapper objectMapper, HttpServletRequest request) {
		this.objectMapper = objectMapper;
		this.request = request;
	}

	private void validateCreateUpdate(ReservationDto reservation) throws ValidationException {
		if (reservation.getFullname() == null || reservation.getFullname().isEmpty()) {
			throw new ValidationException("fullname is required");
		}
	}

	public ResponseEntity<Object> createReservation(
			@ApiParam(value = "Reservation details", required = true) @Valid @RequestBody ReservationDto body)
			throws ValidationException {
//		Reservation reservation = new Reservation(body.getFullname(), body.getEmail(), dates);
		validateCreateUpdate(body);
		ApiErrorDetails error = new ApiErrorDetails("test", "super test");
		return new ResponseEntity<Object>(error, new HttpHeaders(), HttpStatus.ACCEPTED);
	}

	public ResponseEntity<Void> deleteReservation(
			@ApiParam(value = "ID of reservation that needs to be deleted", required = true) @PathVariable("reservationId") Long reservationId) {
		String accept = request.getHeader("Accept");
		return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
	}

	public ResponseEntity<Void> getReservation(
			@ApiParam(value = "ID of reservation requested", required = true) @PathVariable("reservationId") Long reservationId) {
		String accept = request.getHeader("Accept");
		return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
	}

	public ResponseEntity<Void> putReservation(
			@ApiParam(value = "ID of reservation that needs to be updated", required = true) @PathVariable("reservationId") Long reservationId,
			@ApiParam(value = "Reservation details", required = true) @Valid @RequestBody ReservationDto body) {
		String accept = request.getHeader("Accept");
		return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
	}

}
