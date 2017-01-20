package de.neozo.rest;


import de.neozo.domain.Node;
import de.neozo.service.NetworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;


@RestController
public class NetworkController {

    private final NetworkService networkService;

    @Autowired
    public NetworkController(NetworkService networkService) {
        this.networkService = networkService;
    }

    @RequestMapping("/get-known-nodes")
    Set<Node> getKnownNodes() {
        return networkService.getKnownNodes();
    }

    @RequestMapping("/add-node")
    void addNode(Node node) {
        networkService.add(node);
    }

    @RequestMapping("/remove-node")
    void removeNode(Node node) {
        networkService.remove(node);
    }

}
