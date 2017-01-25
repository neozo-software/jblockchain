package de.neozo.blockchain.rest;

import de.neozo.blockchain.domain.Node;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;


@RunWith(SpringRunner.class)
@RestClientTest()
public class NodeControllerTests {

    private final static String ENDPOINT = "http://localhost:8080/node";

    private RestTemplate restTemplate;

    @Before
    public void setUp() throws Exception {
        restTemplate = new RestTemplate();
    }

    @Test
    @Ignore
    public void addCheckRemove() throws UnknownHostException, MalformedURLException {
        Node node = new Node(new URL("neozo.de"));

        restTemplate.put(ENDPOINT, node);

        List<Node> nodes = Arrays.asList(restTemplate.getForObject(ENDPOINT, Node[].class));
        Assert.assertTrue(nodes.contains(node));

        restTemplate.postForLocation(ENDPOINT + "/remove", node);
    }


}

