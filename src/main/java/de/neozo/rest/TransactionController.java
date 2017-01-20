package de.neozo.rest;


import de.neozo.domain.Transaction;
import de.neozo.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;


@RestController
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @RequestMapping("/get-transaction-pool")
    Set<Transaction> getTransactionPool() {
        return transactionService.getTransactionPool();
    }


    @RequestMapping("/add-transaction")
    void addTransaction(Transaction transaction) {
        transactionService.add(transaction);
    }

    @RequestMapping("/publish-transaction")
    void publishTransaction(Transaction transaction) {
        // broadcast transaction to all nodes
    }

}
