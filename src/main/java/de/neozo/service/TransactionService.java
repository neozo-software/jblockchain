package de.neozo.service;


import de.neozo.domain.Transaction;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;


@Service
public class TransactionService {

    private Set<Transaction> transactionPool = new HashSet<>();


    public Set<Transaction> getTransactionPool() {
        return transactionPool;
    }

    public void add(Transaction transaction) {
        transactionPool.add(transaction);
    }
}
