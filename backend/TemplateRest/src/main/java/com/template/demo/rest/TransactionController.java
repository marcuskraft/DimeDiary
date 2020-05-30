package com.template.demo.rest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.template.api.TransactionApi;
import com.template.model.Transaction;
import com.template.model.Transactions;

@RequestMapping("/api")
@Controller
public class TransactionController implements TransactionApi {

	@Override
	public ResponseEntity<Transactions> transactionGet() {

		final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		final List<Transaction> transactions = new ArrayList<Transaction>();
		for (int i = 1; i <= 31; i++) {
			final LocalDate date = LocalDate.of(2020, 0, i);
			final Transaction transaction = new Transaction();
			transaction.amount(Integer.valueOf(i));
			transaction.date(dateTimeFormatter.format(date));

			transactions.add(transaction);
		}

		return null;
	}

}
