package de.neozo.blockchain.service;


import de.neozo.blockchain.Config;
import de.neozo.blockchain.domain.Block;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
public class BlockService {

    private List<Block> blockchain = new ArrayList<>();

    public List<Block> getBlockchain() {
        return blockchain;
    }

    public boolean append(Block block) {
        if (verify(block)) {
            blockchain.add(block);
            return true;
        }
        return false;
    }

    private boolean verify(Block block) {
        // references last block in chain
        if (blockchain.size() > 0) {
            byte[] lastBlockInChainHash = blockchain.get(blockchain.size() - 1).getHash();
            if (!Arrays.equals(block.getPreviousBlockHash(), lastBlockInChainHash)) {
                return false;
            }
        } else {
            if (block.getPreviousBlockHash() != null) {
                return false;
            }
        }

        // considered difficulty
        for (int i = 0; i < Config.DIFICULTY; i++) {
            if (block.getHash()[i] != 0) {
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

        return true;
    }
}
