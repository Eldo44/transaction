package com.company.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.company.model.Statistics;

import com.company.service.StatisticsService;

@RestController
public class StatisticsController {

	@Autowired
	private StatisticsService service;

	@RequestMapping(value = "/statistics", method = RequestMethod.GET, produces = "application/json")
	public Statistics getStatistics() {
		return service.getStatisticsForTheLastMinute();
	}

}
