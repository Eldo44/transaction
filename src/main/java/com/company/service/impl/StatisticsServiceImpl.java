package com.company.service.impl;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.model.Statistics;
import com.company.model.Transaction;
import com.company.service.StatisticsService;
import com.company.service.TransactionService;
import com.company.summary.BigDecimalSummaryStatistics;

@Service
public class StatisticsServiceImpl implements StatisticsService {

	private static final Logger logger = LoggerFactory.getLogger(StatisticsServiceImpl.class);
	@Autowired
	TransactionService transactionService;
	private final Function<BigDecimalSummaryStatistics, Statistics> CALCULATE_STATISTICS_FUNCTION = summary -> {
		Statistics statistics = new Statistics();
		statistics.setCount(summary.getCount());
		statistics.setMax(summary.getMax());
		statistics.setMin(summary.getMin());
		statistics.setSum(summary.getSum());
		statistics.setAvg(summary.getAverage());
		statistics.round();
		return statistics;

	};

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public Statistics getStatisticsForTheLastMinute() {

		ZonedDateTime computedTime = ZonedDateTime.now(ZoneOffset.UTC).minusMinutes(1);

		BigDecimalSummaryStatistics summary = transactionService.getTransactions().stream()
				.filter(transaction -> transaction.getTimestamp().isAfter(computedTime)).map(Transaction::getAmount)
				.collect(BigDecimalSummaryStatistics.collect());
		if (summary.getCount() == 0) {
			// Returning Empty Statistics
			logger.debug("Returning Empty Statistics ");
			return Statistics.initializeWithAllZeroValues();
		}
		return CALCULATE_STATISTICS_FUNCTION.apply(summary);
	}

	void setTransactionService(TransactionService transactionService) {
		this.transactionService = transactionService;
	}

}
