package io.campsite.api.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.campsite.api.ReservationApi;
import io.campsite.api.error.ValidationException;
import io.campsite.api.helper.ValidationHelper;
import io.campsite.api.model.ReservationDto;
import io.campsite.db.dao.ReservationDao;
import io.campsite.db.model.Reservation;
import io.swagger.annotations.ApiParam;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2019-01-04T21:32:25.955Z")

@RestController
public class ReservationApiController implements ReservationApi {

	private static final Logger log = LoggerFactory.getLogger(ReservationApiController.class);

	private final ObjectMapper objectMapper;
	private final HttpServletRequest request;
	private final ReservationDao reservationDao;

	@Autowired
	public ReservationApiController(ObjectMapper objectMapper, HttpServletRequest request,
			ReservationDao reservationDao) {
		this.objectMapper = objectMapper;
		this.request = request;
		this.reservationDao = reservationDao;
	}

	private Reservation getValidatedReservation(ReservationDto reservation) throws ValidationException {
		String fullname = ValidationHelper.required(reservation.getFullname(), "fullname");
		String email = ValidationHelper.required(reservation.getEmail(), "email");
		ValidationHelper.requireContent(reservation.getEmail(), "@", "email");
		ValidationHelper.required(reservation.getSinceDate(), "sinceDate");
		LocalDate sinceDate = ValidationHelper.requireValidDate(reservation.getSinceDate(), "sinceDate");
		ValidationHelper.required(reservation.getUntilDate(), "untilDate");
		LocalDate untilDate = ValidationHelper.requireValidDate(reservation.getUntilDate(), "untilDate");
		if (sinceDate.isAfter(untilDate)) {
			throw new ValidationException("sinceDate must be before untilDate");
		}
		if (sinceDate.plusDays(2).isBefore(untilDate)) {
			throw new ValidationException("reservation must for maximum 3 days");
		}
		if (!sinceDate.isAfter(LocalDate.now())) {
			throw new ValidationException("reservation must be minimum 1 day ahead of arrival");
		}
		if (sinceDate.isAfter(LocalDate.now().plusMonths(1))) {
			throw new ValidationException("reservation must be maximun 1 month in advance");
		}
		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd");
		ArrayList<String> dates = new ArrayList<>();
		LocalDate date = sinceDate;
		while (!date.isAfter(untilDate)) {
			dates.add(date.format(formatter));
			date = date.plusDays(1);
		}
		Reservation result = new Reservation(fullname, email, dates);
		return result;
	}

	private ReservationDto convertToDto(Reservation reservation) {
		ReservationDto result = new ReservationDto();
//		result.setId(reservation.getId());
		return result;
	}

	public ResponseEntity<ReservationDto> createReservation(
			@ApiParam(value = "Reservation details", required = true) @Valid @RequestBody ReservationDto body)
			throws ValidationException {
		Reservation reservation = getValidatedReservation(body);
		reservationDao.save(reservation);
		return new ResponseEntity<ReservationDto>(HttpStatus.CREATED);
	}

	public ResponseEntity<Void> deleteReservation(
			@ApiParam(value = "ID of reservation that needs to be deleted", required = true) @PathVariable("reservationId") Long reservationId) {
		String accept = request.getHeader("Accept");
		return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
	}

	public ResponseEntity<ReservationDto> getReservation(
			@ApiParam(value = "ID of reservation requested", required = true) @PathVariable("reservationId") Long reservationId) {
		String accept = request.getHeader("Accept");
		if (accept != null && accept.contains("application/json")) {
			try {
				return new ResponseEntity<ReservationDto>(objectMapper.readValue(
						"{  \"untilDate\" : null,  \"id\" : \"0\",  \"fullname\" : \"fullname\",  \"sinceDate\" : { },  \"email\" : \"email\"}",
						ReservationDto.class), HttpStatus.NOT_IMPLEMENTED);
			} catch (IOException e) {
				log.error("Couldn't serialize response for content type application/json", e);
				return new ResponseEntity<ReservationDto>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		return new ResponseEntity<ReservationDto>(HttpStatus.NOT_IMPLEMENTED);
	}

	public ResponseEntity<ReservationDto> putReservation(
			@ApiParam(value = "ID of reservation that needs to be updated", required = true) @PathVariable("reservationId") Long reservationId,
			@ApiParam(value = "Reservation details", required = true) @Valid @RequestBody ReservationDto body) {
		String accept = request.getHeader("Accept");
		if (accept != null && accept.contains("application/json")) {
			try {
				return new ResponseEntity<ReservationDto>(objectMapper.readValue(
						"{  \"untilDate\" : null,  \"id\" : \"0\",  \"fullname\" : \"fullname\",  \"sinceDate\" : { },  \"email\" : \"email\"}",
						ReservationDto.class), HttpStatus.NOT_IMPLEMENTED);
			} catch (IOException e) {
				log.error("Couldn't serialize response for content type application/json", e);
				return new ResponseEntity<ReservationDto>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		return new ResponseEntity<ReservationDto>(HttpStatus.NOT_IMPLEMENTED);
	}

}
