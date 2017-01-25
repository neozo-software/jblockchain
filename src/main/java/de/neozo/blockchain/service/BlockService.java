package de.neozo.blockchain.service;


import de.neozo.blockchain.Config;
import de.neozo.blockchain.domain.Block;
import de.neozo.blockchain.domain.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@Service
public class BlockService {

    private final static Logger LOG = LoggerFactory.getLogger(BlockService.class);

    private final TransactionService transactionService;

    private List<Block> blockchain = new ArrayList<>();

    @Autowired
    public BlockService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public List<Block> getBlockchain() {
        return blockchain;
    }

    public Block getLastBlock() {
        if (blockchain.isEmpty()) {
            return null;
        }
        return blockchain.get(blockchain.size() - 1);
    }

    public synchronized boolean append(Block block) {
        if (verify(block)) {
            blockchain.add(block);

            // remove transactions from pool
            block.getTransactions().forEach(transactionService::remove);
            return true;
        }
        return false;
    }

    public void retrieveBlockchain(Node node, RestTemplate restTemplate) {
        Block[] blocks = restTemplate.getForObject(node.getAddress() + "/block", Block[].class);
        Collections.addAll(blockchain, blocks);
        LOG.info("Retrieved " + blocks.length + " blocks from node " + node.getAddress());
    }


    private boolean verify(Block block) {
        // references last block in chain
        if (blockchain.size() > 0) {
            byte[] lastBlockInChainHash = getLastBlock().getHash();
            if (!Arrays.equals(block.getPreviousBlockHash(), lastBlockInChainHash)) {
                return false;
            }
        } else {
            if (block.getPreviousBlockHash() != null) {
                return false;
            }
        }

        // correct hashes
        if (!Arrays.equals(block.getMerkleRoot(), block.calculateMerkleRoot())) {
            return false;
        }
        if (!Arrays.equals(block.getHash(), block.calculateHash())) {
            return false;
        }

        // transaction limit
        if (block.getTransactions().size() > Config.MAX_TRANSACTIONS_PER_BLOCK) {
            return false;
        }

        // all transactions in pool
        if (!transactionService.containsAll(block.getTransactions())) {
            return false;
        }

        // considered difficulty
        if (block.getLeadingZerosCount() < Config.DIFFICULTY) {
            return false;
        }

        return true;
    }
}
