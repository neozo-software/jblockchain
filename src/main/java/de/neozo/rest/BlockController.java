package de.neozo.rest;


import de.neozo.domain.Block;
import de.neozo.service.BlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class BlockController {

    private final BlockService blockService;

    @Autowired
    public BlockController(BlockService blockService) {
        this.blockService = blockService;
    }

    @RequestMapping("/get-blockchain")
    List<Block> getBlockchain() {
        return blockService.getBlockchain();
    }

    @RequestMapping("/add-block")
    void addBlock(Block block) {
        blockService.append(block);
    }

    @RequestMapping("/publish-block")
    void publishBlock(Block block) {
        // broadcast block to all nodes
    }
}
