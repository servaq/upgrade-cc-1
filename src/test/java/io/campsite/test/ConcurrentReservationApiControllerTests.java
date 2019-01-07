package io.campsite.test;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.apache.catalina.connector.Response;
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
public class ConcurrentReservationApiControllerTests {

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
	public void checkConcurrency() throws Exception {
		JSONObject json = new JSONObject();
		json.put("fullname", "Unit test");
		json.put("email", "unit@test");
		json.put("sinceDate", LocalDate.now().plusDays(2).format(dateFormatter));
		json.put("untilDate", LocalDate.now().plusDays(4).format(dateFormatter));
		Callable<Integer> task = () -> {
			MvcResult result = this.mockMvc
					.perform(post("/reservation").contentType(MediaType.APPLICATION_JSON).content(json.toString()))
					.andDo(print()).andReturn();
			return result.getResponse().getStatus();
		};
		final int testSize = 500;
		ExecutorService executor = Executors.newFixedThreadPool(testSize);
		ArrayList<Callable<Integer>> callables = new ArrayList<>(testSize);
		for (int i = 0; i < testSize; i++) {
			callables.add(task);
		}
		List<Integer> result = executor.invokeAll(callables).stream().map(future -> {
			try {
				return future.get();
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
		}).collect(Collectors.toList());
		assertEquals("Reservation created count", 1,
				result.stream().filter(item -> item == Response.SC_CREATED).count());
		assertEquals("Reservation not created count", testSize - 1,
				result.stream().filter(item -> item == Response.SC_BAD_REQUEST).count());
	}

}
