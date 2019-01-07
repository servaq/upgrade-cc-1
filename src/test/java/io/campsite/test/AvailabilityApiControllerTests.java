package io.campsite.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import io.campsite.db.dao.ReservationDao;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AvailabilityApiControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ReservationDao reservationDao;

	private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("YYYY-MM-dd");

	@Before
	public void cleanCollection() {
		reservationDao.cleanCollection();
	}

	@Test
	public void checkFullAvailability() throws Exception {
		JSONObject json = new JSONObject();
		MvcResult result = this.mockMvc
				.perform(get("/availability").contentType(MediaType.APPLICATION_JSON).content(json.toString()))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.items").isArray()).andReturn();
		JSONObject jsonResult = new JSONObject(result.getResponse().getContentAsString());
		JSONArray resultItems = (JSONArray) jsonResult.get("items");
		LocalDate sinceDate = LocalDate.now().plusDays(1);
		LocalDate untilDate = LocalDate.now().plusMonths(1);
		long expectedLength = sinceDate.until(untilDate, ChronoUnit.DAYS) + 1;
		assertEquals("result items length", resultItems.length(), expectedLength);
	}

	@Test
	public void checkValidations() throws Exception {
		JSONObject json = new JSONObject();
		json.put("sinceDate", "2019-99-99");
		this.mockMvc.perform(get("/availability").contentType(MediaType.APPLICATION_JSON).content(json.toString()))
				.andDo(print()).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("sinceDate requires valid format YYYY-MM-DD"));
		json.put("sinceDate", LocalDate.now().plusDays(10).format(dateFormatter));
		json.put("untilDate", "2019-99-99");
		this.mockMvc.perform(get("/availability").contentType(MediaType.APPLICATION_JSON).content(json.toString()))
				.andDo(print()).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("untilDate requires valid format YYYY-MM-DD"));
		json.put("untilDate", "2018-01-01");
		this.mockMvc.perform(get("/availability").contentType(MediaType.APPLICATION_JSON).content(json.toString()))
				.andDo(print()).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("sinceDate must not be after untilDate"));
		json.put("untilDate", LocalDate.now().plusDays(10).format(dateFormatter));
		this.mockMvc.perform(get("/availability").contentType(MediaType.APPLICATION_JSON).content(json.toString()))
				.andDo(print()).andExpect(status().isOk());
	}

	private void checkDateRangeCase(JSONObject json, LocalDate expectedSinceDate, LocalDate expectedUntilDate)
			throws Exception {
		MvcResult result = this.mockMvc
				.perform(get("/availability").contentType(MediaType.APPLICATION_JSON).content(json.toString()))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.items").isArray()).andReturn();
		JSONObject jsonResult = new JSONObject(result.getResponse().getContentAsString());
		JSONArray resultItems = (JSONArray) jsonResult.get("items");
		String resultSinceDate = null;
		String resultUntilDate = null;
		String date;
		for (int i = 0; i < resultItems.length(); i++) {
			date = resultItems.getString(i);
			if (resultSinceDate == null || resultSinceDate.compareTo(date) > 0) {
				resultSinceDate = date;
			}
			if (resultUntilDate == null || resultUntilDate.compareTo(date) < 0) {
				resultUntilDate = date;
			}
		}
		assertEquals("sinceDate check", expectedSinceDate == null ? null : expectedSinceDate.format(dateFormatter),
				resultSinceDate);
		assertEquals("untilDate check", expectedUntilDate == null ? null : expectedUntilDate.format(dateFormatter),
				resultUntilDate);
	}

	@Test
	public void checkDateRange() throws Exception {
		LocalDate sinceDate = LocalDate.now().plusDays(1);
		LocalDate untilDate = LocalDate.now().plusMonths(1);
		JSONObject json = new JSONObject();
		json.put("sinceDate", sinceDate.minusDays(10).format(dateFormatter));
		json.put("untilDate", sinceDate.minusDays(10).format(dateFormatter));
		checkDateRangeCase(json, null, null);
		json.put("sinceDate", sinceDate.plusDays(40).format(dateFormatter));
		json.put("untilDate", sinceDate.plusDays(40).format(dateFormatter));
		checkDateRangeCase(json, null, null);
		json.put("sinceDate", sinceDate.minusDays(10).format(dateFormatter));
		json.put("untilDate", sinceDate.plusDays(40).format(dateFormatter));
		checkDateRangeCase(json, sinceDate, untilDate);
		json.put("sinceDate", sinceDate.format(dateFormatter));
		json.put("untilDate", untilDate.format(dateFormatter));
		checkDateRangeCase(json, sinceDate, untilDate);
		json.put("sinceDate", sinceDate.plusDays(10).format(dateFormatter));
		json.put("untilDate", untilDate.plusDays(10).format(dateFormatter));
		checkDateRangeCase(json, sinceDate.plusDays(10), untilDate);
		json.put("sinceDate", sinceDate.minusDays(10).format(dateFormatter));
		json.put("untilDate", untilDate.minusDays(10).format(dateFormatter));
		checkDateRangeCase(json, sinceDate, untilDate.minusDays(10));
		json.put("sinceDate", sinceDate.plusDays(10).format(dateFormatter));
		json.put("untilDate", untilDate.minusDays(10).format(dateFormatter));
		checkDateRangeCase(json, sinceDate.plusDays(10), untilDate.minusDays(10));
		json.put("sinceDate", sinceDate.format(dateFormatter));
		json.put("untilDate", sinceDate.format(dateFormatter));
		checkDateRangeCase(json, sinceDate, sinceDate);
		json.put("sinceDate", untilDate.format(dateFormatter));
		json.put("untilDate", untilDate.format(dateFormatter));
		checkDateRangeCase(json, untilDate, untilDate);
	}

	private boolean jsonArrayContains(JSONArray jsonArray, String contains) throws JSONException {
		for (int i = 0; i < jsonArray.length(); i++) {
			if (contains.equals(jsonArray.getString(i))) {
				return true;
			}
		}
		return false;
	}

	@Test
	public void checkPartialAvailability() throws Exception {
		LocalDate sinceDate = LocalDate.now().plusDays(1);
		LocalDate untilDate = LocalDate.now().plusMonths(1);
		LocalDate sinceDateReq = LocalDate.now().plusDays(2);
		LocalDate untilDateReq = LocalDate.now().plusDays(4);
		JSONObject json = new JSONObject();
		json.put("fullname", "Unit test");
		json.put("email", "unit@test");
		json.put("sinceDate", sinceDateReq.format(dateFormatter));
		json.put("untilDate", untilDateReq.format(dateFormatter));
		this.mockMvc.perform(post("/reservation").contentType(MediaType.APPLICATION_JSON).content(json.toString()))
				.andDo(print()).andExpect(status().isCreated());
		MvcResult result = this.mockMvc
				.perform(get("/availability").contentType(MediaType.APPLICATION_JSON).content(json.toString()))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.items").isArray()).andReturn();
		JSONObject jsonResult = new JSONObject(result.getResponse().getContentAsString());
		JSONArray resultItems = (JSONArray) jsonResult.get("items");
		assertTrue("items must be empty", resultItems.length() == 0);
		JSONObject jsonFullAvailability = new JSONObject();
		result = this.mockMvc
				.perform(get("/availability").contentType(MediaType.APPLICATION_JSON)
						.content(jsonFullAvailability.toString()))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.items").isArray()).andReturn();
		jsonResult = new JSONObject(result.getResponse().getContentAsString());
		resultItems = (JSONArray) jsonResult.get("items");
		LocalDate date = sinceDate;
		while (!date.isAfter(untilDate)) {
			if (date.isBefore(sinceDateReq) || date.isAfter(untilDateReq)) {
				assertTrue("items must contain: " + date.format(dateFormatter),
						jsonArrayContains(resultItems, date.format(dateFormatter)));
			}
			date = date.plusDays(1);
		}
	}

	@Test
	public void checkNoneAvailability() throws Exception {
		LocalDate sinceDate = LocalDate.now().plusDays(1);
		LocalDate untilDate = LocalDate.now().plusMonths(1);
		JSONObject json = new JSONObject();
		json.put("fullname", "Unit test");
		json.put("email", "unit@test");
		LocalDate date = sinceDate;
		while (!date.isAfter(untilDate)) {
			json.put("sinceDate", date.format(dateFormatter));
			json.put("untilDate", date.format(dateFormatter));
			this.mockMvc.perform(post("/reservation").contentType(MediaType.APPLICATION_JSON).content(json.toString()))
					.andDo(print()).andExpect(status().isCreated());
			date = date.plusDays(1);
		}
		json = new JSONObject();
		MvcResult result = this.mockMvc
				.perform(get("/availability").contentType(MediaType.APPLICATION_JSON).content(json.toString()))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.items").isArray()).andReturn();
		JSONObject jsonResult = new JSONObject(result.getResponse().getContentAsString());
		JSONArray resultItems = (JSONArray) jsonResult.get("items");
		assertTrue("items must be empty", resultItems.length() == 0);
	}

}
