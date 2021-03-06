package io.campsite.api;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.campsite.api.error.ApiNotFoundException;
import io.campsite.api.error.ApiValidationException;
import io.campsite.api.model.ApiErrorDto;
import io.campsite.api.model.ReservationDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2019-01-05T03:01:39.117Z")

@Api(value = "reservation")
public interface ReservationApi {

	@ApiOperation(value = "Creates a reservation", nickname = "createReservation", notes = "Creates a reservation for the given date range.", response = ReservationDto.class, tags = {
			"reservation", })
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Successful operation", response = ReservationDto.class),
			@ApiResponse(code = 400, message = "Invalid reservation data", response = ApiErrorDto.class) })
	@RequestMapping(value = "/reservation", produces = { "application/json" }, consumes = {
			"application/json" }, method = RequestMethod.POST)
	ResponseEntity<ReservationDto> createReservation(
			@ApiParam(value = "Reservation details", required = true) @Valid @RequestBody ReservationDto body)
			throws ApiValidationException;

	@ApiOperation(value = "Cancels a reservation", nickname = "deleteReservation", notes = "Cancels a reservation with the given ID", tags = {
			"reservation", })
	@ApiResponses(value = { @ApiResponse(code = 204, message = "Successful operation"),
			@ApiResponse(code = 404, message = "Invalid reservation ID", response = ApiErrorDto.class) })
	@RequestMapping(value = "/reservation/{reservationId}", produces = {
			"application/json" }, method = RequestMethod.DELETE)
	ResponseEntity<Void> deleteReservation(
			@ApiParam(value = "ID of reservation that needs to be deleted", required = true) @PathVariable("reservationId") String reservationId)
			throws ApiNotFoundException;

	@ApiOperation(value = "Get reservation details", nickname = "getReservation", notes = "Get reservation details for the given reservation ID.", response = ReservationDto.class, tags = {
			"reservation", })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successful operation", response = ReservationDto.class),
			@ApiResponse(code = 404, message = "Invalid reservation ID", response = ApiErrorDto.class) })
	@RequestMapping(value = "/reservation/{reservationId}", produces = { "application/json" }, consumes = {
			"application/json" }, method = RequestMethod.GET)
	ResponseEntity<ReservationDto> getReservation(
			@ApiParam(value = "ID of reservation requested", required = true) @PathVariable("reservationId") String reservationId)
			throws ApiNotFoundException;

	@ApiOperation(value = "Updates a reservation", nickname = "updateReservation", notes = "Updates the reservation with the given data.", response = ReservationDto.class, tags = {
			"reservation", })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successful operation", response = ReservationDto.class),
			@ApiResponse(code = 400, message = "Invalid reservation data", response = ApiErrorDto.class),
			@ApiResponse(code = 404, message = "Invalid reservation ID", response = ApiErrorDto.class) })
	@RequestMapping(value = "/reservation/{reservationId}", produces = { "application/json" }, consumes = {
			"application/json" }, method = RequestMethod.PUT)
	ResponseEntity<ReservationDto> updateReservation(
			@ApiParam(value = "ID of reservation that needs to be updated", required = true) @PathVariable("reservationId") String reservationId,
			@ApiParam(value = "Reservation details", required = true) @Valid @RequestBody ReservationDto body)
			throws ApiNotFoundException, ApiValidationException;

}
