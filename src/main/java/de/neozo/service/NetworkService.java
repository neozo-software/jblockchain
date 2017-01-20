package de.neozo.service;


import de.neozo.domain.Node;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
public class NetworkService {

    private Set<Node> knownNodes;

    public Set<Node> getKnownNodes() {
        return knownNodes;
    }

    public void add(Node node) {
        knownNodes.add(node);
    }

    public void remove(Node node) {
        knownNodes.remove(node);
    }
}
