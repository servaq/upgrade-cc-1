package io.campsite.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.campsite.api.error.ApiValidationException;
import io.campsite.api.model.ApiErrorDto;
import io.campsite.api.model.DateRangeDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2019-01-04T21:32:25.955Z")

@Api(value = "availability")
public interface AvailabilityApi {

	@ApiOperation(value = "Finds Campsite availability", nickname = "getAvailability", notes = "Provides information of the availability of the campsite for a given date range with the default being 1 month.", response = String.class, responseContainer = "List", tags = {
			"reservation", })
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successful operation", response = String.class, responseContainer = "List"),
			@ApiResponse(code = 400, message = "Invalid date range values", response = ApiErrorDto.class) })
	@RequestMapping(value = "/availability", produces = { "application/json" }, consumes = {
			"application/json" }, method = RequestMethod.GET)
	ResponseEntity<List<String>> getAvailability(
			@ApiParam(value = "Date range to get availability") @Valid @RequestBody DateRangeDto body)
			throws ApiValidationException;

}
