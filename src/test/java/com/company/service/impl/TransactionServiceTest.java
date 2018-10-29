package com.company.service.impl;

import org.junit.Test;
import org.springframework.util.CollectionUtils;
import com.company.model.Transaction;
import com.company.service.impl.TransactionServiceImpl;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class TransactionServiceTest {

	TransactionServiceImpl transactionService = new TransactionServiceImpl();

	@Test
	public void shouldSaveTransaction() {

		// Given
		Transaction transaction = new Transaction(BigDecimal.valueOf(45.43), ZonedDateTime.now(ZoneOffset.UTC));
		assertTrue("Transactions must be empty", CollectionUtils.isEmpty(transactionService.getTransactions()));

		// When
		boolean success = transactionService.saveTransaction(transaction);

		// Then
		assertTrue("Expected to insert transaction successfuly", success);
		assertEquals(1, transactionService.getTransactions().size());
	}

	@Test
	public void shouldDeleteTransactions() {
		// Given
		Transaction transaction = new Transaction(BigDecimal.valueOf(45.43), ZonedDateTime.now(ZoneOffset.UTC));

		// When
		transactionService.saveTransaction(transaction);
		transactionService.deleteAllTransactions();

		// Then
		assertTrue("Transactions must be empty", CollectionUtils.isEmpty(transactionService.getTransactions()));

	}

	@Test
	public void shouldReturnTransactionsWithSameTimestamp() {
		ZonedDateTime timeNow = ZonedDateTime.now(ZoneOffset.UTC);

		// Given
		Transaction transaction1 = new Transaction(BigDecimal.valueOf(45.43), timeNow);
		Transaction transaction2 = new Transaction(BigDecimal.valueOf(95.43), timeNow);

		// When
		transactionService.saveTransaction(transaction1);
		transactionService.saveTransaction(transaction2);

		// Then
		assertEquals(2, transactionService.getTransactions().size());

	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldNotSaveFutureTransaction() {
		ZonedDateTime futureTime = ZonedDateTime.now(ZoneOffset.UTC).plusMinutes(2);
		// Given
		Transaction transaction = new Transaction(BigDecimal.valueOf(45.43), futureTime);

		// When
		transactionService.saveTransaction(transaction);

		// Then expect IllegalArgumentException

	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowIllegalArgumentException() {
		// Given
		Transaction transaction = new Transaction(BigDecimal.valueOf(45.43), null);

		// When
		transactionService.saveTransaction(transaction);

		// Then expect IllegalArgumentException
	}

	@Test
	public void shouldDeleteTransactionsOlderThanAMinute() throws InterruptedException {
		// Given
		ZonedDateTime timestmp = ZonedDateTime.now(ZoneOffset.UTC).minusSeconds(58);
		Transaction transaction = new Transaction(BigDecimal.valueOf(45.43), timestmp);

		// When
		transactionService.saveTransaction(transaction);
		assertEquals(1, transactionService.getTransactions().size());
		Thread.sleep(2000);
		transactionService.deleteTransactionsOlderThanAMinute();

		// Then
		assertEquals(0, transactionService.getTransactions().size());
	}

}
