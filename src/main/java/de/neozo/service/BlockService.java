package de.neozo.service;


import de.neozo.domain.Block;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class BlockService {

    private List<Block> blockchain;

    public List<Block> getBlockchain() {
        return blockchain;
    }

    public void append(Block block) {
        blockchain.add(block);
    }
}
