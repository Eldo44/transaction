package com.company.summary;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collector;

import com.company.util.MathUtils;

/**
 * Like {@code DoubleSummaryStatistics}, {@code IntSummaryStatistics}, and
 * {@code LongSummaryStatistics}, but for {@link BigDecimal}.
 */
public class BigDecimalSummaryStatistics implements Consumer<BigDecimal> {

	public static Collector<BigDecimal, BigDecimalSummaryStatistics, BigDecimalSummaryStatistics> collect() {
		return Collector.of(BigDecimalSummaryStatistics::new, BigDecimalSummaryStatistics::accept,
				BigDecimalSummaryStatistics::merge);
	}

	private BigDecimal sum = BigDecimal.ZERO, min, max;
	private long count;

	public void accept(BigDecimal value) {
		if (count == 0) {
			Objects.requireNonNull(value);
			count = 1;
			sum = value;
			min = value;
			max = value;
		} else {
			sum = sum.add(value);
			if (min.compareTo(value) > 0)
				min = value;
			if (max.compareTo(value) < 0)
				max = value;
			count++;
		}
	}

	public BigDecimalSummaryStatistics merge(BigDecimalSummaryStatistics next) {
		if (next.count > 0) {
			if (count == 0) {
				count = next.count;
				sum = next.sum;
				min = next.min;
				max = next.max;
			} else {
				sum = sum.add(next.sum);
				if (min.compareTo(next.min) > 0)
					min = next.min;
				if (max.compareTo(next.max) < 0)
					max = next.max;
				count += next.count;
			}
		}
		return this;
	}

	public long getCount() {
		return count;
	}

	public BigDecimal getSum() {
		return sum;
	}

	public BigDecimal getAverage() {
		return count < 2 ? sum : sum.divide(BigDecimal.valueOf(count), 2, MathUtils.BIGDECIMAL_ROUNDING_MODE);
	}

	public BigDecimal getMin() {
		return min;
	}

	public BigDecimal getMax() {
		return max;
	}

	@Override
	public String toString() {
		return count == 0 ? "empty" : (count + " elements between " + min + " and " + max + ", sum=" + sum);
	}
}
