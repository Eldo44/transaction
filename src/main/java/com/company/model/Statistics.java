package com.company.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.company.json.converter.BigDecimalSerializer;

import static com.company.util.MathUtils.BIGDECIMAL_SCALE;
import static com.company.util.MathUtils.BIGDECIMAL_ROUNDING_MODE;

public class Statistics implements Serializable {

	private static final long serialVersionUID = -8935319307484705072L;
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal sum;
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal avg;
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal max;
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal min;
	private long count;

	@Override
	public String toString() {
		return "Statistics [sum=" + sum + ", avg=" + avg + ", max=" + max + ", min=" + min + ", count=" + count + "]";
	}

	public void round() {
		sum = sum.setScale(BIGDECIMAL_SCALE, BIGDECIMAL_ROUNDING_MODE);
		avg = avg.setScale(BIGDECIMAL_SCALE, BIGDECIMAL_ROUNDING_MODE);
		max = max.setScale(BIGDECIMAL_SCALE, BIGDECIMAL_ROUNDING_MODE);
		min = min.setScale(BIGDECIMAL_SCALE, BIGDECIMAL_ROUNDING_MODE);

	}

	public static Statistics initializeWithAllZeroValues() {
		Statistics statistics = new Statistics();
		statistics.setMax(BigDecimal.ZERO);
		statistics.setMin(BigDecimal.ZERO);
		statistics.setAvg(BigDecimal.ZERO);
		statistics.setSum(BigDecimal.ZERO);
		return statistics;
	}

	public Statistics() {
	}

	public static Statistics of(double sum, double avg, double max, double min, long count) {
		Statistics statistics = new Statistics();
		statistics.sum = new BigDecimal(sum);
		statistics.avg = new BigDecimal(avg);
		;
		statistics.max = new BigDecimal(max);
		;
		statistics.min = new BigDecimal(min);
		;
		statistics.count = count;
		return statistics;
	}

	public BigDecimal getSum() {
		return sum;
	}

	public void setSum(BigDecimal sum) {
		this.sum = sum;
	}

	public BigDecimal getAvg() {
		return avg;
	}

	public void setAvg(BigDecimal avg) {
		this.avg = avg;
	}

	public BigDecimal getMax() {
		return max;
	}

	public void setMax(BigDecimal max) {
		this.max = max;
	}

	public BigDecimal getMin() {
		return min;
	}

	public void setMin(BigDecimal min) {
		this.min = min;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}
}
