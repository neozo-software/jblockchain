package de.neozo.rest;


import de.neozo.domain.Block;
import de.neozo.service.BlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("block")
public class BlockController {

    private final BlockService blockService;

    @Autowired
    public BlockController(BlockService blockService) {
        this.blockService = blockService;
    }

    @RequestMapping
    List<Block> getBlockchain() {
        return blockService.getBlockchain();
    }

    @RequestMapping(method = RequestMethod.PUT)
    void addBlock(@RequestBody Block block) {
        blockService.append(block);
    }

    @RequestMapping(path = "/publish", method = RequestMethod.POST)
    void publishBlock(@RequestBody Block block) {
        // broadcast block to all nodes
    }
}
