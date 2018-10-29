package com.company.service.impl;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

import com.company.model.Statistics;
import com.company.model.Transaction;
import com.company.service.StatisticsService;
import com.company.service.TransactionService;
import com.company.service.impl.StatisticsServiceImpl;
import com.company.service.impl.TransactionServiceImpl;

import static org.junit.Assert.assertEquals;

public class StatisticsServiceTest {

	private final StatisticsService statisticsService = new StatisticsServiceImpl();
	private final TransactionService transactionService = new TransactionServiceImpl();
	{
		((StatisticsServiceImpl) statisticsService).setTransactionService(transactionService);
	}

	@Test
	public void shouldReturnStatisticsForTheLastMinute() {

		ZonedDateTime currentTime = ZonedDateTime.now(ZoneOffset.UTC);

		// Given
		Transaction transaction1 = new Transaction(BigDecimal.valueOf(10), currentTime);
		Transaction transaction2 = new Transaction(BigDecimal.valueOf(20), currentTime.minusSeconds(50));
		Transaction transaction3 = new Transaction(BigDecimal.valueOf(30), currentTime.minusSeconds(61));

		// When
		transactionService.saveTransaction(transaction1);
		transactionService.saveTransaction(transaction2);
		transactionService.saveTransaction(transaction3);

		// Then
		Statistics statitics = statisticsService.getStatisticsForTheLastMinute();
		assertEquals(30, statitics.getSum().intValue());
		assertEquals(2, statitics.getCount());
		assertEquals(10, statitics.getMin().intValue());
		assertEquals(20, statitics.getMax().intValue());
		assertEquals(15, statitics.getAvg().intValue());

	}

	@Test
	public void shouldCalculateStatisticsWithRounding() {
		ZonedDateTime currentTime = ZonedDateTime.now(ZoneOffset.UTC);
		// Given
		Transaction transaction = new Transaction(BigDecimal.valueOf(30.345), currentTime);
		transactionService.saveTransaction(transaction);

		// When
		Statistics statitics = statisticsService.getStatisticsForTheLastMinute();

		// Then
		assertEquals(BigDecimal.valueOf(30.35), statitics.getSum());
		assertEquals(1, statitics.getCount());
		assertEquals(BigDecimal.valueOf(30.35), statitics.getMin());
		assertEquals(BigDecimal.valueOf(30.35), statitics.getMax());
		assertEquals(BigDecimal.valueOf(30.35), statitics.getAvg());

	}

	@Test(expected = Test.None.class /* no exception expected */)
	public void shouldWorkConcurrently() {
		ZonedDateTime currentTime = ZonedDateTime.now(ZoneOffset.UTC);
		ZonedDateTime testTillTime = ZonedDateTime.now(ZoneOffset.UTC).plusSeconds(10);
		Transaction transaction1 = new Transaction(BigDecimal.valueOf(30.345), currentTime);
		Transaction transaction2 = new Transaction(BigDecimal.valueOf(30.345), currentTime.minusSeconds(1));
		Transaction transaction3 = new Transaction(BigDecimal.valueOf(30.345), currentTime.minusSeconds(58));
		Transaction transaction4 = new Transaction(BigDecimal.valueOf(30.345), currentTime.minusSeconds(59));
		Transaction transaction5 = new Transaction(BigDecimal.valueOf(30.345), currentTime.minusSeconds(59));

		ExecutorService service = Executors.newFixedThreadPool(10);
		try {
			while (ZonedDateTime.now(ZoneOffset.UTC).isBefore(testTillTime)) {
				service.submit(() -> {
					transactionService.saveTransaction(transaction1);
					transactionService.saveTransaction(transaction2);
					transactionService.saveTransaction(transaction3);
					transactionService.saveTransaction(transaction4);
					transactionService.saveTransaction(transaction5);
				});

				service.submit(() -> {
					transactionService.deleteTransactionsOlderThanAMinute();
					transactionService.deleteAllTransactions();
				});

				service.submit(() -> {
					statisticsService.getStatisticsForTheLastMinute();
				});

			}
		} finally {
			service.shutdownNow();
		}
	}

}
