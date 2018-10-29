package com.company.service.impl;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.company.model.Transaction;
import com.company.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

	private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

	private final ConcurrentHashMap<ZonedDateTime, List<Transaction>> transactions = new ConcurrentHashMap<>();

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public boolean saveTransaction(Transaction transaction) {
		if (transaction.getAmount() == null || transaction.getTimestamp() == null) {
			throw new IllegalArgumentException("Amount/timestamp cannot be empty");
		}

		ZonedDateTime transactionTimestamp = transaction.getTimestamp();
		ZonedDateTime currentTimestamp = ZonedDateTime.now(ZoneOffset.UTC);
		ZonedDateTime timeBefore60Seconds = currentTimestamp.minusMinutes(1);

		// Check if transaction time is older than 60 seconds
		if (transactionTimestamp.isBefore(timeBefore60Seconds)) {
			logger.debug("Discarding old Transaction, " + transaction);
			return false;
		}

		// Check if transaction time is from the future
		if (transactionTimestamp.isAfter(currentTimestamp)) {
			logger.error("Discarding future Transaction, " + transaction);
			throw new IllegalArgumentException("Future transactions not allowed");
		}
		// Atomically insert/update the value to the given key
		transactions.compute(transactionTimestamp, (key, value) -> {

			List<Transaction> transactions = new ArrayList<>();
			transactions.add(transaction);
			if (value != null) {
				value.addAll(transactions);
				return value;
			} else {
				return transactions;
			}
		});
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public List<Transaction> getTransactions() {
		List<Transaction> list = transactions.values().stream().flatMap(List::stream).collect(Collectors.toList());
		return Collections.unmodifiableList(list);
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public void deleteAllTransactions() {
		transactions.clear();
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public void deleteTransactionsOlderThanAMinute() {
		Enumeration<ZonedDateTime> timestamps = transactions.keys();
		ZonedDateTime timeBefore60seconds = ZonedDateTime.now(ZoneOffset.UTC).minusMinutes(1);
		logger.debug("Deleting Transactions older than  " + timeBefore60seconds);
		while (timestamps.hasMoreElements()) {
			ZonedDateTime timestamp = timestamps.nextElement();
			if (timestamp.isBefore(timeBefore60seconds)) {
				transactions.remove(timestamp);
				logger.debug("Removed Transaction " + timestamp);
			}
		}
	}

}
