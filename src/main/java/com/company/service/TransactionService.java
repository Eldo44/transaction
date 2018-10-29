package com.company.service;

import java.util.List;

import com.company.model.Transaction;

/**
 * A transaction service which is responsible for saving, retrieving and
 * deleting transactions
 * 
 * It follows the UTC timezone for its calculations
 * 
 * @author Eldo
 *
 */
public interface TransactionService {
	/**
	 * Saves the transaction into memory. Transactions older than a minute is not
	 * saved
	 * 
	 * @param transaction
	 * @return false when the transaction is older than a minute
	 * @throws IllegalArgumentException
	 *             when timestamp or amount is null or future timestamp
	 */
	boolean saveTransaction(Transaction transaction);

	/**
	 * Returns all transactions from the memory. The returned List is an
	 * <a href="../List.html#unmodifiable">unmodifiable List</a>. Transactions
	 * inserted/removed at the time of retrieval may not be returned
	 * 
	 * @return
	 */
	List<Transaction> getTransactions();

	/**
	 * Deletes all the transactions in memory. Transactions inserted during the
	 * deletion, may or not be deleted.
	 */
	void deleteAllTransactions();

	/**
	 * Deletes transactions older than a minute in memory. Transactions inserted
	 * during the deletion, may or not be deleted.
	 */
	void deleteTransactionsOlderThanAMinute();
}
