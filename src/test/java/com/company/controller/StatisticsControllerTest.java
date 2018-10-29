package com.company.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.company.model.Statistics;
import com.company.service.StatisticsService;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(StatisticsController.class)
public class StatisticsControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private StatisticsService statisticsService;

	@Test
	public void shouldReturnRightStatistics() throws Exception {
		when(statisticsService.getStatisticsForTheLastMinute()).thenReturn(Statistics.of(50, 25, 10, 10, 5));

		mvc.perform(get("/statistics").accept("application/json")).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith("application/json")).andExpect(jsonPath("count", is(5)))
				.andExpect(jsonPath("sum", is("50.00"))).andExpect(jsonPath("avg", is("25.00")))
				.andExpect(jsonPath("max", is("10.00"))).andExpect(jsonPath("min", is("10.00")));
	}

	@Test
	public void shouldReturnZeroStatistics() throws Exception {
		when(statisticsService.getStatisticsForTheLastMinute()).thenReturn(Statistics.initializeWithAllZeroValues());

		mvc.perform(get("/statistics").accept("application/json")).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith("application/json")).andExpect(jsonPath("count", is(0)))
				.andExpect(jsonPath("sum", is("0.00"))).andExpect(jsonPath("avg", is("0.00")))
				.andExpect(jsonPath("max", is("0.00"))).andExpect(jsonPath("min", is("0.00")));
	}
}
