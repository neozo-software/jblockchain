package de.neozo.jblockchain.node.service;


import de.neozo.jblockchain.common.domain.Node;
import de.neozo.jblockchain.node.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
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
public class NodeService implements ApplicationListener<EmbeddedServletContainerInitializedEvent> {

    private final static Logger LOG = LoggerFactory.getLogger(NodeService.class);

    private final BlockService blockService;
    private final TransactionService transactionService;

    private Node self;
    private Set<Node> knownNodes = new HashSet<>();
    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public NodeService(BlockService blockService, TransactionService transactionService) {
        this.blockService = blockService;
        this.transactionService = transactionService;
    }

    @Override
    public void onApplicationEvent(EmbeddedServletContainerInitializedEvent embeddedServletContainerInitializedEvent) {
        int port = embeddedServletContainerInitializedEvent.getEmbeddedServletContainer().getPort();

        self = getSelfNode(port);
        LOG.info("Self address: " + self.getAddress());
        Node masterNode = getMasterNode();

        if (self.equals(masterNode)) {
            LOG.info("Running as master node, nothing to init");
        } else {
            knownNodes.add(masterNode);

            // retrieve data
            retrieveKnownNodes(masterNode, restTemplate);
            blockService.retrieveBlockchain(masterNode, restTemplate);
            transactionService.retrieveTransactions(masterNode, restTemplate);

            // publish self
            broadcastPut("node", self);
        }
    }

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

    public void broadcastPut(String endpoint, Object data) {
        knownNodes.parallelStream().forEach(node -> restTemplate.put(node.getAddress() + "/" + endpoint, data));
    }

    public void broadcastPost(String endpoint, Object data) {
        knownNodes.parallelStream().forEach(node -> restTemplate.postForLocation(node.getAddress() + "/" + endpoint, data));
    }

    public void retrieveKnownNodes(Node node, RestTemplate restTemplate) {
        Node[] nodes = restTemplate.getForObject(node.getAddress() + "/node", Node[].class);
        Collections.addAll(knownNodes, nodes);
        LOG.info("Retrieved " + nodes.length + " nodes from node " + node.getAddress());
    }

    private Node getSelfNode(int port) {
        // TODO discover own remote address by querying a webservice like http://checkip.amazonaws.com or master node
        try {
            return new Node(new URL("http", "localhost", port, ""));
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
