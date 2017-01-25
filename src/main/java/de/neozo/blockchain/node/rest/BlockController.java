package de.neozo.blockchain.node.rest;


import de.neozo.blockchain.common.domain.Block;
import de.neozo.blockchain.node.service.BlockService;
import de.neozo.blockchain.node.service.MiningService;
import de.neozo.blockchain.node.service.NodeService;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


@RestController
@RequestMapping("block")
public class BlockController {

    private static final Logger LOG = LoggerFactory.getLogger(BlockController.class);

    private final BlockService blockService;
    private final NodeService nodeService;
    private final MiningService miningService;

    @Autowired
    public BlockController(BlockService blockService, NodeService nodeService, MiningService miningService) {
        this.blockService = blockService;
        this.nodeService = nodeService;
        this.miningService = miningService;
    }

    @RequestMapping
    List<Block> getBlockchain() {
        return blockService.getBlockchain();
    }

    @RequestMapping(method = RequestMethod.PUT)
    void addBlock(@RequestBody Block block, @RequestParam(required = false) Boolean publish, HttpServletResponse response) {
        LOG.info("Add block " + Base64.encodeBase64String(block.getHash()));
        boolean success = blockService.append(block);

        if (success) {
            response.setStatus(HttpServletResponse.SC_ACCEPTED);

            if (publish != null && publish) {
                nodeService.broadcastPut("block", block);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        }
    }

    @RequestMapping(path = "start-miner")
    public void startMiner() {
        miningService.startMiner();
    }

    @RequestMapping(path = "stop-miner")
    public void stopMiner() {
        miningService.stopMiner();
    }

}
