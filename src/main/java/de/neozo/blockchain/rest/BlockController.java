package de.neozo.blockchain.rest;


import de.neozo.blockchain.domain.Block;
import de.neozo.blockchain.service.BlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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
    void addBlock(@RequestBody Block block, @RequestParam(required = false) Boolean publish, HttpServletResponse response) {
        blockService.append(block);
    }

}
