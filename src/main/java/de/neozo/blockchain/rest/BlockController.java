package de.neozo.blockchain.rest;


import de.neozo.blockchain.domain.Block;
import de.neozo.blockchain.service.BlockService;
import de.neozo.blockchain.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


@RestController
@RequestMapping("block")
public class BlockController {

    private final BlockService blockService;
    private final NodeService nodeService;

    @Autowired
    public BlockController(BlockService blockService, NodeService nodeService) {
        this.blockService = blockService;
        this.nodeService = nodeService;
    }

    @RequestMapping
    List<Block> getBlockchain() {
        return blockService.getBlockchain();
    }

    @RequestMapping(method = RequestMethod.PUT)
    void addBlock(@RequestBody Block block, @RequestParam(required = false) Boolean publish, HttpServletResponse response) {
        boolean success = blockService.append(block);

        if (success) {
            response.setStatus(HttpServletResponse.SC_ACCEPTED);

            if (publish != null && publish) {
                nodeService.broadcast("block", block);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        }
    }

}
