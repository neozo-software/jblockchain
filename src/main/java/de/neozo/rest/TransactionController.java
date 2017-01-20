package de.neozo.rest;


import de.neozo.domain.Transaction;
import de.neozo.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;


@RestController()
@RequestMapping("transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @RequestMapping
    Set<Transaction> getTransactionPool() {
        return transactionService.getTransactionPool();
    }


    @RequestMapping(method = RequestMethod.PUT)
    void addTransaction(@RequestBody Transaction transaction) {
        transactionService.add(transaction);
    }

    @RequestMapping(path = "/publish", method = RequestMethod.POST)
    void publishTransaction(@RequestBody Transaction transaction) {
        // broadcast transaction to all nodes
    }

}
