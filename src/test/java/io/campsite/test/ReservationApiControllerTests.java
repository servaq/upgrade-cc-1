package io.campsite.test;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
public class ReservationApiControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ReservationDao reservationDao;

	private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("YYYY-MM-dd");

	@Before
	public void dropCollection() {
		reservationDao.dropCollection();
	}

	@Test
	public void test() throws Exception {
		getReservationNotFound();
		deleteReservationNotFound();
		String reservationId = createReservation();
		updateReservation(reservationId);
		deleteReservationOk(reservationId);
	}

	private void getReservationNotFound() throws Exception {
		this.mockMvc.perform(get("/reservation/inexistentId").contentType(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isNotFound()).andExpect(jsonPath("$.message").value("reservation not found"));
	}

	private void deleteReservationNotFound() throws Exception {
		this.mockMvc.perform(delete("/reservation/inexistentId").contentType(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isNotFound()).andExpect(jsonPath("$.message").value("reservation not found"));
	}

	private void deleteReservationOk(String reservationId) throws Exception {
		this.mockMvc.perform(delete("/reservation/" + reservationId).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isNoContent());
	}

	private String createReservation() throws Exception {
		JSONObject json = new JSONObject();
		this.mockMvc.perform(post("/reservation").contentType(MediaType.APPLICATION_JSON).content(json.toString()))
				.andDo(print()).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("fullname is required"));
		json.put("fullname", "");
		this.mockMvc.perform(post("/reservation").contentType(MediaType.APPLICATION_JSON).content(json.toString()))
				.andDo(print()).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("fullname is required"));
		json.put("fullname", "Unit test");
		this.mockMvc.perform(post("/reservation").contentType(MediaType.APPLICATION_JSON).content(json.toString()))
				.andDo(print()).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("email is required"));
		json.put("email", "");
		this.mockMvc.perform(post("/reservation").contentType(MediaType.APPLICATION_JSON).content(json.toString()))
				.andDo(print()).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("email is required"));
		json.put("email", "unit");
		this.mockMvc.perform(post("/reservation").contentType(MediaType.APPLICATION_JSON).content(json.toString()))
				.andDo(print()).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("email requires '@'"));
		json.put("email", "unit@test");
		this.mockMvc.perform(post("/reservation").contentType(MediaType.APPLICATION_JSON).content(json.toString()))
				.andDo(print()).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("sinceDate is required"));
		json.put("sinceDate", "");
		this.mockMvc.perform(post("/reservation").contentType(MediaType.APPLICATION_JSON).content(json.toString()))
				.andDo(print()).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("sinceDate is required"));
		json.put("sinceDate", "2019-99-99");
		this.mockMvc.perform(post("/reservation").contentType(MediaType.APPLICATION_JSON).content(json.toString()))
				.andDo(print()).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("sinceDate requires valid format YYYY-MM-DD"));
		json.put("sinceDate", LocalDate.now().plusDays(10).format(dateFormatter));
		this.mockMvc.perform(post("/reservation").contentType(MediaType.APPLICATION_JSON).content(json.toString()))
				.andDo(print()).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("untilDate is required"));
		json.put("untilDate", "");
		this.mockMvc.perform(post("/reservation").contentType(MediaType.APPLICATION_JSON).content(json.toString()))
				.andDo(print()).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("untilDate is required"));
		json.put("untilDate", "2019-99-99");
		this.mockMvc.perform(post("/reservation").contentType(MediaType.APPLICATION_JSON).content(json.toString()))
				.andDo(print()).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("untilDate requires valid format YYYY-MM-DD"));
		json.put("untilDate", "2018-01-01");
		this.mockMvc.perform(post("/reservation").contentType(MediaType.APPLICATION_JSON).content(json.toString()))
				.andDo(print()).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("sinceDate must not be after untilDate"));
		json.put("untilDate", LocalDate.now().plusDays(20).format(dateFormatter));
		this.mockMvc.perform(post("/reservation").contentType(MediaType.APPLICATION_JSON).content(json.toString()))
				.andDo(print()).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("reservation must for maximum 3 days"));
		json.put("sinceDate", LocalDate.now().format(dateFormatter));
		json.put("untilDate", LocalDate.now().plusDays(1).format(dateFormatter));
		this.mockMvc.perform(post("/reservation").contentType(MediaType.APPLICATION_JSON).content(json.toString()))
				.andDo(print()).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("reservation must be minimum 1 day ahead of arrival"));
		json.put("sinceDate", LocalDate.now().plusMonths(2).format(dateFormatter));
		json.put("untilDate", LocalDate.now().plusMonths(2).format(dateFormatter));
		this.mockMvc.perform(post("/reservation").contentType(MediaType.APPLICATION_JSON).content(json.toString()))
				.andDo(print()).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("reservation must be maximun 1 month in advance"));
		json.put("sinceDate", LocalDate.now().plusDays(2).format(dateFormatter));
		json.put("untilDate", LocalDate.now().plusDays(4).format(dateFormatter));
		MvcResult result = this.mockMvc
				.perform(post("/reservation").contentType(MediaType.APPLICATION_JSON).content(json.toString()))
				.andDo(print()).andExpect(status().isCreated()).andReturn();
		// Validates requested reservation is equals to response reservation
		JSONObject jsonResult = new JSONObject(result.getResponse().getContentAsString());
		assertEquals("request.fullname != response.fullname", jsonResult.get("fullname"), json.get("fullname"));
		assertEquals("request.email != response.email", jsonResult.get("email"), json.get("email"));
		assertEquals("request.sinceDate != response.sinceDate", jsonResult.get("sinceDate"), json.get("sinceDate"));
		assertEquals("request.untilDate != response.untilDate", jsonResult.get("untilDate"), json.get("untilDate"));
		// Get reservation ID
		String reservationId = jsonResult.getString("id");
		// Validates GET reservation is equals to original reservation
		result = this.mockMvc.perform(get("/reservation/" + reservationId).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk()).andReturn();
		jsonResult = new JSONObject(result.getResponse().getContentAsString());
		assertEquals("request.fullname != response.fullname", jsonResult.get("fullname"), json.get("fullname"));
		assertEquals("request.email != response.email", jsonResult.get("email"), json.get("email"));
		assertEquals("request.sinceDate != response.sinceDate", jsonResult.get("sinceDate"), json.get("sinceDate"));
		assertEquals("request.untilDate != response.untilDate", jsonResult.get("untilDate"), json.get("untilDate"));
		return reservationId;
	}

	private void updateReservation(String reservationId) throws Exception {
		JSONObject json = new JSONObject();
		this.mockMvc
				.perform(put("/reservation/" + reservationId).contentType(MediaType.APPLICATION_JSON)
						.content(json.toString()))
				.andDo(print()).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("fullname is required"));
		json.put("fullname", "");
		this.mockMvc
				.perform(put("/reservation/" + reservationId).contentType(MediaType.APPLICATION_JSON)
						.content(json.toString()))
				.andDo(print()).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("fullname is required"));
		json.put("fullname", "Unit test 2");
		this.mockMvc
				.perform(put("/reservation/" + reservationId).contentType(MediaType.APPLICATION_JSON)
						.content(json.toString()))
				.andDo(print()).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("email is required"));
		json.put("email", "");
		this.mockMvc
				.perform(put("/reservation/" + reservationId).contentType(MediaType.APPLICATION_JSON)
						.content(json.toString()))
				.andDo(print()).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("email is required"));
		json.put("email", "unit");
		this.mockMvc
				.perform(put("/reservation/" + reservationId).contentType(MediaType.APPLICATION_JSON)
						.content(json.toString()))
				.andDo(print()).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("email requires '@'"));
		json.put("email", "unit@test2");
		this.mockMvc
				.perform(put("/reservation/" + reservationId).contentType(MediaType.APPLICATION_JSON)
						.content(json.toString()))
				.andDo(print()).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("sinceDate is required"));
		json.put("sinceDate", "");
		this.mockMvc
				.perform(put("/reservation/" + reservationId).contentType(MediaType.APPLICATION_JSON)
						.content(json.toString()))
				.andDo(print()).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("sinceDate is required"));
		json.put("sinceDate", "2019-99-99");
		this.mockMvc
				.perform(put("/reservation/" + reservationId).contentType(MediaType.APPLICATION_JSON)
						.content(json.toString()))
				.andDo(print()).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("sinceDate requires valid format YYYY-MM-DD"));
		json.put("sinceDate", LocalDate.now().plusDays(10).format(dateFormatter));
		this.mockMvc
				.perform(put("/reservation/" + reservationId).contentType(MediaType.APPLICATION_JSON)
						.content(json.toString()))
				.andDo(print()).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("untilDate is required"));
		json.put("untilDate", "");
		this.mockMvc
				.perform(put("/reservation/" + reservationId).contentType(MediaType.APPLICATION_JSON)
						.content(json.toString()))
				.andDo(print()).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("untilDate is required"));
		json.put("untilDate", "2019-99-99");
		this.mockMvc
				.perform(put("/reservation/" + reservationId).contentType(MediaType.APPLICATION_JSON)
						.content(json.toString()))
				.andDo(print()).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("untilDate requires valid format YYYY-MM-DD"));
		json.put("untilDate", "2018-01-01");
		this.mockMvc
				.perform(put("/reservation/" + reservationId).contentType(MediaType.APPLICATION_JSON)
						.content(json.toString()))
				.andDo(print()).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("sinceDate must not be after untilDate"));
		json.put("untilDate", LocalDate.now().plusDays(20).format(dateFormatter));
		this.mockMvc
				.perform(put("/reservation/" + reservationId).contentType(MediaType.APPLICATION_JSON)
						.content(json.toString()))
				.andDo(print()).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("reservation must for maximum 3 days"));
		json.put("sinceDate", LocalDate.now().format(dateFormatter));
		json.put("untilDate", LocalDate.now().plusDays(1).format(dateFormatter));
		this.mockMvc
				.perform(put("/reservation/" + reservationId).contentType(MediaType.APPLICATION_JSON)
						.content(json.toString()))
				.andDo(print()).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("reservation must be minimum 1 day ahead of arrival"));
		json.put("sinceDate", LocalDate.now().plusMonths(2).format(dateFormatter));
		json.put("untilDate", LocalDate.now().plusMonths(2).format(dateFormatter));
		this.mockMvc
				.perform(put("/reservation/" + reservationId).contentType(MediaType.APPLICATION_JSON)
						.content(json.toString()))
				.andDo(print()).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("reservation must be maximun 1 month in advance"));
		json.put("sinceDate", LocalDate.now().plusDays(1).format(dateFormatter));
		json.put("untilDate", LocalDate.now().plusDays(2).format(dateFormatter));
		MvcResult result = this.mockMvc.perform(
				put("/reservation/" + reservationId).contentType(MediaType.APPLICATION_JSON).content(json.toString()))
				.andDo(print()).andExpect(status().isOk()).andReturn();
		// Validates requested reservation is equals to response reservation
		JSONObject jsonResult = new JSONObject(result.getResponse().getContentAsString());
		assertEquals("request.id != response.id", jsonResult.get("id"), reservationId);
		assertEquals("request.fullname != response.fullname", jsonResult.get("fullname"), json.get("fullname"));
		assertEquals("request.email != response.email", jsonResult.get("email"), json.get("email"));
		assertEquals("request.sinceDate != response.sinceDate", jsonResult.get("sinceDate"), json.get("sinceDate"));
		assertEquals("request.untilDate != response.untilDate", jsonResult.get("untilDate"), json.get("untilDate"));
		// Validates GET reservation is equals to original reservation
		result = this.mockMvc.perform(get("/reservation/" + reservationId).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk()).andReturn();
		jsonResult = new JSONObject(result.getResponse().getContentAsString());
		assertEquals("request.fullname != response.fullname", jsonResult.get("fullname"), json.get("fullname"));
		assertEquals("request.email != response.email", jsonResult.get("email"), json.get("email"));
		assertEquals("request.sinceDate != response.sinceDate", jsonResult.get("sinceDate"), json.get("sinceDate"));
		assertEquals("request.untilDate != response.untilDate", jsonResult.get("untilDate"), json.get("untilDate"));
	}

}
