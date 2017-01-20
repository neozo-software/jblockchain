package de.neozo.service;


import de.neozo.domain.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;


@Service
public class TransactionService {

    private Set<Transaction> transactionPool;


    public Set<Transaction> getTransactionPool() {
        return transactionPool;
    }

    public void add(Transaction transaction) {
        transactionPool.add(transaction);
    }
}
