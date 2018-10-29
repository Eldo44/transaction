package com.company.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class Transaction implements Serializable {
	private static final long serialVersionUID = -554299187852281262L;
	@NotNull
	private final BigDecimal amount;
	@PastOrPresent
	@NotNull
	private final ZonedDateTime timestamp;

	@JsonCreator
	public Transaction(@JsonProperty("amount") BigDecimal amount, @JsonProperty("timestamp") ZonedDateTime timestamp) {
		this.amount = amount;
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "Transaction [amount=" + amount + ", timestamp=" + timestamp + "]";
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public ZonedDateTime getTimestamp() {
		return timestamp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Transaction other = (Transaction) obj;
		return Objects.equals(this.amount, other.amount) && Objects.equals(this.timestamp, other.timestamp);
	}

}
