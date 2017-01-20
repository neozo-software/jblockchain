package de.neozo.domain;


import java.time.Instant;
import java.util.List;

public class Block {

    private byte[] hash;
    private Block previousBlock;
    private List<Transaction> transactions;
    private byte[] merkleRoot;
    private long none;
    private Instant timestamp;

}
