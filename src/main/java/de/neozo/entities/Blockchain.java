package de.neozo.entities;

import java.util.List;

public class Blockchain {

    private List<Block> blocks;

    public void append(Block block) {
        blocks.add(block);
    }
}
