package io.campsite.api.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.campsite.api.AvailabilityApi;
import io.campsite.api.error.ApiValidationException;
import io.campsite.api.helper.ValidationHelper;
import io.campsite.api.model.AvailableDatesDto;
import io.campsite.api.model.DateRangeDto;
import io.campsite.db.dao.ReservationDao;
import io.swagger.annotations.ApiParam;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2019-01-06T19:03:16.515Z")

@Controller
public class AvailabilityApiController implements AvailabilityApi {

	private static final Logger log = LoggerFactory.getLogger(AvailabilityApiController.class);
	private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("YYYY-MM-dd");

	private final ObjectMapper objectMapper;
	private final HttpServletRequest request;
	private final ReservationDao reservationDao;

	@Autowired
	public AvailabilityApiController(ObjectMapper objectMapper, HttpServletRequest request,
			ReservationDao reservationDao) {
		this.objectMapper = objectMapper;
		this.request = request;
		this.reservationDao = reservationDao;
	}

	private List<LocalDate> getValidatedDateRange(DateRangeDto dateRange) throws ApiValidationException {
		LocalDate sinceDate = LocalDate.now().plusDays(1);
		LocalDate untilDate = LocalDate.now().plusMonths(1);
		LocalDate sinceDateReq = null;
		LocalDate untilDateReq = null;
		if (!ValidationHelper.isEmpty(dateRange.getSinceDate())) {
			sinceDateReq = ValidationHelper.requireValidDate(dateRange.getSinceDate(), "sinceDate");
		}
		if (!ValidationHelper.isEmpty(dateRange.getUntilDate())) {
			untilDateReq = ValidationHelper.requireValidDate(dateRange.getUntilDate(), "untilDate");
		}
		if (sinceDateReq != null && untilDateReq != null && sinceDateReq.isAfter(untilDateReq)) {
			throw new ApiValidationException("sinceDate must not be after untilDate");
		}
		if (sinceDateReq != null) {
			if (sinceDateReq.isAfter(untilDate)) {
				return new ArrayList<>();
			}
			if (sinceDateReq.isAfter(sinceDate)) {
				sinceDate = sinceDateReq;
			}
		}
		if (untilDateReq != null) {
			if (untilDateReq.isBefore(sinceDate)) {
				return new ArrayList<>();
			}
			if (untilDateReq.isBefore(untilDate)) {
				untilDate = untilDateReq;
			}
		}
		ArrayList<LocalDate> result = new ArrayList<>();
		result.add(sinceDate);
		result.add(untilDate);
		return result;
	}

	private List<String> getNotAvailableDates(List<LocalDate> dateRange) {
		String sinceDate = dateRange.get(0).format(dateFormatter);
		String untilDate = dateRange.get(1).format(dateFormatter);
		Query query = new Query();
		query.addCriteria(Criteria.where("dates").gte(sinceDate).lte(untilDate));
		return reservationDao.findAllDistinct("dates", query, String.class);
	}

	public ResponseEntity<AvailableDatesDto> getAvailability(
			@ApiParam(value = "Date range to get availability") @Valid @RequestBody DateRangeDto body)
			throws ApiValidationException {
		ArrayList<String> result = new ArrayList<>();
		List<LocalDate> dateRange = getValidatedDateRange(body);
		if (!dateRange.isEmpty()) {
			List<String> notAvailableDates = getNotAvailableDates(dateRange);
			LocalDate date = dateRange.get(0);
			String dateString;
			while (!date.isAfter(dateRange.get(1))) {
				dateString = date.format(dateFormatter);
				if (!notAvailableDates.contains(dateString)) {
					result.add(dateString);
				}
				date = date.plusDays(1);
			}
		}
		AvailableDatesDto availableDatesDto = new AvailableDatesDto();
		availableDatesDto.setItems(result);
		return new ResponseEntity<AvailableDatesDto>(availableDatesDto, HttpStatus.OK);
	}

}
