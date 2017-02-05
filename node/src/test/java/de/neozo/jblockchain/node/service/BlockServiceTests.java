package de.neozo.jblockchain.node.service;


import de.neozo.jblockchain.common.SignatureUtils;
import de.neozo.jblockchain.common.domain.Address;
import de.neozo.jblockchain.common.domain.Block;
import de.neozo.jblockchain.common.domain.Transaction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlockServiceTests {

    @Autowired private BlockService blockService;
    @Autowired private TransactionService transactionService;
    @Autowired private AddressService addressService;

    private Address address;
    private KeyPair keyPair;

    @Before
    public void setUp() throws Exception {
        keyPair = SignatureUtils.generateKeyPair();
        address = new Address("Max Mustermann", keyPair.getPublic().getEncoded());
        addressService.add(address);
    }

    @Test
    public void addBlock_invalidHash() throws Exception {
        Block block = new Block(null, generateTransactions(1), 42);
        boolean success = blockService.append(block);
        Assert.assertFalse(success);
    }

    @Test
    public void addBlock_invalidLimitExceeded() throws Exception {
        Block block = new Block(null, generateTransactions(6), 42);
        boolean success = blockService.append(block);
        Assert.assertFalse(success);
    }

    private List<Transaction> generateTransactions(int count) throws Exception {
        List<Transaction> transactions = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            String text = "Hello " + i;
            byte[] signature = SignatureUtils.sign(text.getBytes(), keyPair.getPrivate().getEncoded());
            Transaction transaction = new Transaction(text, address.getHash(), signature);

            transactionService.add(transaction);
            transactions.add(transaction);
        }
        return transactions;
    }

}
