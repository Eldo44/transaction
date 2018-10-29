package com.company.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.company.model.Transaction;
import com.company.service.TransactionService;

@RestController
public class TransactionController {

	@Autowired
	private TransactionService service;

	@RequestMapping(value = "/transactions", method = RequestMethod.POST)
	public ResponseEntity<?> postTransaction(@Valid @RequestBody Transaction transaction) {
		boolean created = service.saveTransaction(transaction);

		if (created) {
			return ResponseEntity.status(HttpStatus.CREATED).body(null);
		} else {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		}
	}

	@RequestMapping(value = "/transactions", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteTransaction() {
		service.deleteAllTransactions();
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(JsonParseException.class)
	public void handleParsingException() {
	}

	@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
	@ExceptionHandler(InvalidFormatException.class)
	public void handleConversionException() {
	}

}
