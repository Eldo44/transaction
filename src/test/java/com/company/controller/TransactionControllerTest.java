package com.company.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import com.company.service.TransactionService;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@RunWith(SpringRunner.class)
@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private TransactionService transactionService;

	private final ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);

	@Test
	public void shouldInValidateWrongEntityRequest() throws Exception {
		mvc.perform(post("/transactions").contentType("application/json")
				// language=JSON
				.content("{}")).andExpect(status().isUnprocessableEntity()).andExpect(content().bytes(new byte[0]));

		verifyZeroInteractions(transactionService);
	}

	@Test
	public void shouldInValidateWrongJsonDocument() throws Exception {
		mvc.perform(post("/transactions").contentType("application/json")
				// language=JSON
				.content("Hello world")).andExpect(status().isBadRequest()).andExpect(content().bytes(new byte[0]));

		verifyZeroInteractions(transactionService);
	}

	@Test
	public void shouldAcceptValidRequestWithPresentTimestamp() throws Exception {

		when(transactionService.saveTransaction(any())).thenReturn(true);
		mvc.perform(post("/transactions").contentType("application/json")
				// language=JSON
				.content("{\"amount\": 55,\"timestamp\": \"" + now.toString() + "\"}")).andExpect(status().isCreated())
				.andExpect(content().bytes(new byte[0]));

		verify(transactionService).saveTransaction(any());
	}

	@Test
	public void shouldAcceptValidRequestWithOldTimestamp() throws Exception {

		when(transactionService.saveTransaction(any())).thenReturn(false);
		mvc.perform(post("/transactions").contentType("application/json")
				// language=JSON
				.content("{\"amount\": 55,\"timestamp\": \"" + now.toString() + "\"}"))
				.andExpect(status().isNoContent()).andExpect(content().bytes(new byte[0]));

		verify(transactionService).saveTransaction(any());
	}

	@Test
	public void shouldDeleteTransactions() throws Exception {
		mvc.perform(delete("/transactions").contentType("application/json")
				// language=JSON
				.content("{}")).andExpect(status().isNoContent()).andExpect(content().bytes(new byte[0]));

		verify(transactionService).deleteAllTransactions();
	}

}
