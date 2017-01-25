package de.neozo.blockchain.node.service;


import de.neozo.blockchain.common.SignatureUtils;
import de.neozo.blockchain.common.domain.Address;
import de.neozo.blockchain.common.domain.Node;
import de.neozo.blockchain.common.domain.Transaction;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;


@Service
public class TransactionService {

    private final static Logger LOG = LoggerFactory.getLogger(TransactionService.class);

    private final AddressService addressService;

    private Set<Transaction> transactionPool = new HashSet<>();

    @Autowired
    public TransactionService(AddressService addressService) {
        this.addressService = addressService;
    }


    public Set<Transaction> getTransactionPool() {
        return transactionPool;
    }

    public synchronized boolean add(Transaction transaction) {
        if (verify(transaction)) {
            transactionPool.add(transaction);
            return true;
        }
        return false;
    }

    public void remove(Transaction transaction) {
        transactionPool.remove(transaction);
    }

    public boolean containsAll(Collection<Transaction> transactions) {
        return transactionPool.containsAll(transactions);
    }

    private boolean verify(Transaction transaction) {
        // correct signature
        Address sender = addressService.getByHash(transaction.getSenderHash());
        if (sender == null) {
            LOG.warn("Unknown address " + Base64.encodeBase64String(transaction.getSenderHash()));
            return false;
        }

        try {
            if (!SignatureUtils.verify(transaction.getSignableData(), transaction.getSignature(), sender.getPublicKey())) {
                LOG.warn("Invalid signature");
                return false;
            }
        } catch (Exception e) {
            LOG.error("Error while verification", e);
            return false;
        }

        // correct hash
        if (!Arrays.equals(transaction.getHash(), transaction.calculateHash())) {
            LOG.warn("Invalid hash");
            return false;
        }

        return true;
    }

    public void retrieveTransactions(Node node, RestTemplate restTemplate) {
        Transaction[] transactions = restTemplate.getForObject(node.getAddress() + "/transaction", Transaction[].class);
        Collections.addAll(transactionPool, transactions);
        LOG.info("Retrieved " + transactions.length + " transactions from node " + node.getAddress());
    }
}
