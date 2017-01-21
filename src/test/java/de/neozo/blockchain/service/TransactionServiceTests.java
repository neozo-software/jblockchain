package de.neozo.blockchain.service;


import de.neozo.blockchain.domain.Address;
import de.neozo.blockchain.domain.Transaction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionServiceTests {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AddressService addressService;

    private Address address;

    @Before
    public void setUp() throws Exception {
        address = new Address("Max Mustermann", new byte[] {1, 2, 3});
        addressService.add(address);
    }

    @Test
    public void addTransaction_invalid() {
        Transaction transaction = new Transaction("Lorem Ipsum", address.getHash(), new byte[] {0});

        boolean success = transactionService.add(transaction);
        Assert.assertFalse(success);
    }
}
