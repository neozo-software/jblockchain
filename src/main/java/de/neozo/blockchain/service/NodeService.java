package de.neozo.blockchain.service;


import de.neozo.blockchain.domain.Node;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;


@Service
public class NodeService {

    private Set<Node> knownNodes = new HashSet<>();

    public Set<Node> getKnownNodes() {
        return knownNodes;
    }

    public void add(Node node) {
        knownNodes.add(node);
    }

    public void remove(Node node) {
        knownNodes.remove(node);
    }

    public void broadcast(String endpoint, Object data) {
        // TODO broadcasting
    }
}
