package com.company.service;

import com.company.model.Statistics;

/**
 * A service to calculate the summary for the transactions in memory.
 * 
 * @author Eldo
 *
 */
public interface StatisticsService {
	/**
	 * Aggregates all the transactions older than a minute.
	 * 
	 * @return Statistics which encapsulates the summary
	 */
	Statistics getStatisticsForTheLastMinute();

}
