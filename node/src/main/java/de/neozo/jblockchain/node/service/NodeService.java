package de.neozo.jblockchain.node.service;


import de.neozo.jblockchain.common.domain.Node;
import de.neozo.jblockchain.node.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PreDestroy;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


@Service
public class NodeService implements ApplicationListener<ServletWebServerInitializedEvent> {

    private final static Logger LOG = LoggerFactory.getLogger(NodeService.class);

    private final BlockService blockService;
    private final TransactionService transactionService;
    private final AddressService addressService;

    private Node self;
    private Set<Node> knownNodes = new HashSet<>();
    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public NodeService(BlockService blockService, TransactionService transactionService, AddressService addressService) {
        this.blockService = blockService;
        this.transactionService = transactionService;
        this.addressService = addressService;
    }

    /**
     * Initial setup, query master Node for
     *  - Other Nodes
     *  - All Addresses
     *  - Current Blockchain
     *  - Transactions in pool
     *  and publish self on all other Nodes
     * @param servletWebServerInitializedEvent serverletContainer for port retrieval
     */
    @Override
    public void onApplicationEvent(ServletWebServerInitializedEvent servletWebServerInitializedEvent) {
        Node masterNode = getMasterNode();

        // construct self node
        String host = retrieveSelfExternalHost(masterNode, restTemplate);
        int port = servletWebServerInitializedEvent.getWebServer().getPort();

        self = getSelfNode(host, port);
        LOG.info("Self address: " + self.getAddress());

        // download data if necessary
        if (self.equals(masterNode)) {
            LOG.info("Running as master node, nothing to init");
        } else {
            knownNodes.add(masterNode);

            // retrieve data
            retrieveKnownNodes(masterNode, restTemplate);
            addressService.retrieveAddresses(masterNode, restTemplate);
            blockService.retrieveBlockchain(masterNode, restTemplate);
            transactionService.retrieveTransactions(masterNode, restTemplate);

            // publish self
            broadcastPut("node", self);
        }
    }

    /**
     * Logout from every other Node before shutdown
     */
    @PreDestroy
    public void shutdown() {
        LOG.info("Shutting down");
        broadcastPost("node/remove", self);
        LOG.info(knownNodes.size() + " informed");
    }


    public Set<Node> getKnownNodes() {
        return knownNodes;
    }

    public synchronized void add(Node node) {
        knownNodes.add(node);
    }

    public synchronized void remove(Node node) {
        knownNodes.remove(node);
    }

    /**
     * Invoke a PUT request on all other Nodes
     * @param endpoint the endpoint for this request
     * @param data the data to send
     */
    public void broadcastPut(String endpoint, Object data) {
        knownNodes.parallelStream().forEach(node -> restTemplate.put(node.getAddress() + "/" + endpoint, data));
    }

    /**
     * Invoke a POST request on all other Nodes
     * @param endpoint the endpoint for this request
     * @param data the data to send
     */
    public void broadcastPost(String endpoint, Object data) {
        knownNodes.parallelStream().forEach(node -> restTemplate.postForLocation(node.getAddress() + "/" + endpoint, data));
    }

    /**
     * Download Nodes from other Node and them to known Nodes
     * @param node Node to query
     * @param restTemplate RestTemplate to use
     */
    public void retrieveKnownNodes(Node node, RestTemplate restTemplate) {
        Node[] nodes = restTemplate.getForObject(node.getAddress() + "/node", Node[].class);
        Collections.addAll(knownNodes, nodes);
        LOG.info("Retrieved " + nodes.length + " nodes from node " + node.getAddress());
    }

    private String retrieveSelfExternalHost(Node node, RestTemplate restTemplate) {
        return restTemplate.getForObject(node.getAddress() + "/node/ip", String.class);
    }

    private Node getSelfNode(String host, int port) {
        try {
            return new Node(new URL("http", host, port, ""));
        } catch (MalformedURLException e) {
            LOG.error("Invalid self URL", e);
            return new Node();
        }
    }

    private Node getMasterNode() {
        try {
            return new Node(new URL(Config.MASTER_NODE_ADDRESS));
        } catch (MalformedURLException e) {
            LOG.error("Invalid master node URL", e);
            return new Node();
        }
    }

}
