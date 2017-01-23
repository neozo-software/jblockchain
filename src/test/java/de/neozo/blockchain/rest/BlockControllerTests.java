package de.neozo.blockchain.rest;

import de.neozo.blockchain.domain.Block;
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
import java.util.Collections;
import java.util.List;


@RunWith(SpringRunner.class)
@RestClientTest()
public class BlockControllerTests {

    private final static String ENDPOINT = "http://localhost:8080/block";

    private RestTemplate restTemplate;

    @Before
    public void setUp() throws Exception {
        restTemplate = new RestTemplate();
    }

    @Test
    @Ignore
    public void addCheck() throws UnknownHostException {
        Block block = new Block(null, Collections.emptyList(), 1337);
        restTemplate.put(ENDPOINT, block);

        List<Block> blocks = Arrays.asList(restTemplate.getForObject(ENDPOINT, Block[].class));
        Assert.assertTrue(blocks.contains(block));
    }


}

