package de.neozo.blockchain.rest;

import de.neozo.blockchain.domain.Transaction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;


@RunWith(SpringRunner.class)
@RestClientTest()
public class TransactionControllerTests {

    private final static String ENDPOINT = "http://localhost:8080/transaction";

    private RestTemplate restTemplate;

    @Before
    public void setUp() throws Exception {
        restTemplate = new RestTemplate();
    }

    @Test
    @Ignore
    public void addCheck() throws UnknownHostException {
        Transaction transaction = new Transaction("Lorem Ipsum", new byte[] {42, 13, 37}, new byte[] {0});
        restTemplate.put(ENDPOINT, transaction);

        List<Transaction> transactions = Arrays.asList(restTemplate.getForObject(ENDPOINT, Transaction[].class));
        Assert.assertTrue(transactions.contains(transaction));

    }


}

