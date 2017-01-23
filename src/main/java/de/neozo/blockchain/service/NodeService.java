package de.neozo.blockchain.service;


import de.neozo.blockchain.domain.Node;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Set;


@Service
public class NodeService {

    private Set<Node> knownNodes = new HashSet<>();

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
        RestTemplate restTemplate = new RestTemplate();
        knownNodes.parallelStream().forEach(node -> restTemplate.put(node.getAddress() + "/" + endpoint, data));
    }
}
