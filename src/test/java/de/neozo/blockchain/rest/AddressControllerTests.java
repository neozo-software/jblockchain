package de.neozo.blockchain.rest;

import de.neozo.blockchain.domain.Address;
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
public class AddressControllerTests {

    private final static String ENDPOINT = "http://localhost:8080/address";

    private RestTemplate restTemplate;

    @Before
    public void setUp() throws Exception {
        restTemplate = new RestTemplate();
    }

    @Test
    @Ignore
    public void addCheck() throws UnknownHostException {
        Address address = new Address("Max Mustermann", new byte[] {1, 2, 3});

        restTemplate.put(ENDPOINT, address);

        List<Address> addresses = Arrays.asList(restTemplate.getForObject(ENDPOINT, Address[].class));
        Assert.assertTrue(addresses.contains(address));

    }


}

