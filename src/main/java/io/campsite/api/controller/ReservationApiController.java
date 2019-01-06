package io.campsite.api.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

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
import io.campsite.api.error.ApiNotFoundException;
import io.campsite.api.error.ApiValidationException;
import io.campsite.api.helper.ValidationHelper;
import io.campsite.api.model.ReservationDto;
import io.campsite.db.dao.ReservationDao;
import io.campsite.db.model.Reservation;
import io.swagger.annotations.ApiParam;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2019-01-05T03:01:39.117Z")

@RestController
public class ReservationApiController implements ReservationApi {

	private static final Logger log = LoggerFactory.getLogger(ReservationApiController.class);
	private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("YYYY-MM-dd");

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

	private Reservation getValidatedReservation(ReservationDto reservation) throws ApiValidationException {
		String fullname = ValidationHelper.required(reservation.getFullname(), "fullname");
		String email = ValidationHelper.required(reservation.getEmail(), "email");
		ValidationHelper.requireContent(reservation.getEmail(), "@", "email");
		ValidationHelper.required(reservation.getSinceDate(), "sinceDate");
		LocalDate sinceDate = ValidationHelper.requireValidDate(reservation.getSinceDate(), "sinceDate");
		ValidationHelper.required(reservation.getUntilDate(), "untilDate");
		LocalDate untilDate = ValidationHelper.requireValidDate(reservation.getUntilDate(), "untilDate");
		if (sinceDate.isAfter(untilDate)) {
			throw new ApiValidationException("sinceDate must not be after untilDate");
		}
		if (sinceDate.plusDays(2).isBefore(untilDate)) {
			throw new ApiValidationException("reservation must for maximum 3 days");
		}
		if (!sinceDate.isAfter(LocalDate.now())) {
			throw new ApiValidationException("reservation must be minimum 1 day ahead of arrival");
		}
		if (sinceDate.isAfter(LocalDate.now().plusMonths(1))) {
			throw new ApiValidationException("reservation must be maximun 1 month in advance");
		}
		ArrayList<String> dates = new ArrayList<>();
		LocalDate date = sinceDate;
		while (!date.isAfter(untilDate)) {
			dates.add(date.format(dateFormatter));
			date = date.plusDays(1);
		}
		Reservation result = new Reservation(fullname, email, dates);
		return result;
	}

	private ReservationDto convertToDto(Reservation reservation) {
		ReservationDto result = new ReservationDto();
		result.setId(reservation.getId());
		result.setFullname(reservation.getFullname());
		result.setEmail(reservation.getEmail());
		result.setSinceDate(Collections.min(reservation.getDates()));
		result.setUntilDate(Collections.max(reservation.getDates()));
		return result;
	}

	public ResponseEntity<ReservationDto> createReservation(
			@ApiParam(value = "Reservation details", required = true) @Valid @RequestBody ReservationDto body)
			throws ApiValidationException {
		Reservation reservation = getValidatedReservation(body);
		reservationDao.save(reservation);
		ReservationDto reservationDto = convertToDto(reservation);
		return new ResponseEntity<ReservationDto>(reservationDto, HttpStatus.CREATED);
	}

	public ResponseEntity<Void> deleteReservation(
			@ApiParam(value = "ID of reservation that needs to be deleted", required = true) @PathVariable("reservationId") String reservationId)
			throws ApiNotFoundException {
		Reservation reservation = new Reservation("", "", new ArrayList<>());
		reservation.setId(reservationId);
		boolean deleted = reservationDao.remove(reservation);
		if (!deleted) {
			throw new ApiNotFoundException("reservation not found");
		} else {
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		}
	}

	public ResponseEntity<ReservationDto> getReservation(
			@ApiParam(value = "ID of reservation requested", required = true) @PathVariable("reservationId") String reservationId)
			throws ApiNotFoundException {
		Reservation reservation = reservationDao.findById(reservationId);
		if (reservation == null) {
			throw new ApiNotFoundException("reservation not found");
		}
		ReservationDto reservationDto = convertToDto(reservation);
		return new ResponseEntity<ReservationDto>(reservationDto, HttpStatus.OK);
	}

	public ResponseEntity<ReservationDto> updateReservation(
			@ApiParam(value = "ID of reservation that needs to be updated", required = true) @PathVariable("reservationId") String reservationId,
			@ApiParam(value = "Reservation details", required = true) @Valid @RequestBody ReservationDto body)
			throws ApiNotFoundException, ApiValidationException {
		Reservation reservation = reservationDao.findById(reservationId);
		if (reservation == null) {
			throw new ApiNotFoundException("reservation not found");
		}
		Reservation newReservation = getValidatedReservation(body);
		reservation.setFullname(newReservation.getFullname());
		reservation.setEmail(newReservation.getEmail());
		reservation.setDates(newReservation.getDates());
		reservationDao.save(reservation);
		ReservationDto reservationDto = convertToDto(reservation);
		return new ResponseEntity<ReservationDto>(reservationDto, HttpStatus.OK);
	}

}
