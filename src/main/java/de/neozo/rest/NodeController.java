package de.neozo.rest;


import de.neozo.domain.Node;
import de.neozo.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;


@RestController
@RequestMapping("node")
public class NodeController {

    private final NodeService nodeService;

    @Autowired
    public NodeController(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    @RequestMapping()
    Set<Node> getNodes() {
        return nodeService.getKnownNodes();
    }

    @RequestMapping(method = RequestMethod.PUT)
    void addNode(@RequestBody Node node) {
        nodeService.add(node);
    }

    @RequestMapping(path = "remove", method = RequestMethod.POST)
    void removeNode(@RequestBody Node node) {
        nodeService.remove(node);
    }

}
