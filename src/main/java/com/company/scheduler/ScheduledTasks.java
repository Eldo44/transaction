package com.company.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.company.service.TransactionService;

@Component
public class ScheduledTasks {

	@Autowired
	private TransactionService transactionService;

	@Scheduled(fixedRate = 60000)
	public void removeOldTransactions() {
		transactionService.deleteTransactionsOlderThanAMinute();
	}

}
