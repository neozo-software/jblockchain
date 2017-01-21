package de.neozo.blockchain.service;


import de.neozo.blockchain.domain.Address;
import de.neozo.blockchain.domain.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;


@Service
public class TransactionService {

    private final static Logger LOG = LoggerFactory.getLogger(TransactionService.class);

    private final AddressService addressService;
    private final SignatureSevice signatureSevice;

    private Set<Transaction> transactionPool = new HashSet<>();

    @Autowired
    public TransactionService(AddressService addressService, SignatureSevice signatureSevice) {
        this.addressService = addressService;
        this.signatureSevice = signatureSevice;
    }


    public Set<Transaction> getTransactionPool() {
        return transactionPool;
    }

    public boolean add(Transaction transaction) {
        if (verify(transaction)) {
            transactionPool.add(transaction);
            return true;
        }
        return false;
    }

    private boolean verify(Transaction transaction) {
        Address sender = addressService.getByHash(transaction.getSenderHash());

        try {
            return signatureSevice.verify(transaction.getSignableData(), transaction.getSignature(), sender.getPublicKey());
        } catch (Exception e) {
            LOG.error("error while verification", e);
        }
        return false;
    }

}
