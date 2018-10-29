package com.company.summary;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BigDecimalSummaryStatisticsTest {

	@Test
	public void shouldGenerateStatistics() {

		// Given
		List<BigDecimal> amounts = Arrays.asList(new BigDecimal(10), new BigDecimal(20), new BigDecimal(30));

		// When
		BigDecimalSummaryStatistics summary = amounts.stream().collect(BigDecimalSummaryStatistics.collect());

		// Then
		assertEquals(3, summary.getCount());
		assertEquals(10, summary.getMin().longValue());
		assertEquals(30, summary.getMax().longValue());
		assertEquals(60, summary.getSum().longValue());
		assertEquals(20, summary.getAverage().longValue());

	}

	@Test
	public void shouldGenerateStatisticsInParallel() {

		// Given
		List<BigDecimal> amounts = Arrays.asList(new BigDecimal(10), new BigDecimal(20), new BigDecimal(30));

		// When
		BigDecimalSummaryStatistics summary = amounts.parallelStream().collect(BigDecimalSummaryStatistics.collect());

		// Then
		assertEquals(3, summary.getCount());
		assertEquals(10, summary.getMin().longValue());
		assertEquals(30, summary.getMax().longValue());
		assertEquals(60, summary.getSum().longValue());
		assertEquals(20, summary.getAverage().longValue());
	}
}
