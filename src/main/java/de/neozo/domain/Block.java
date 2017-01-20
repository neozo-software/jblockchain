package de.neozo.domain;


import java.util.Arrays;
import java.util.List;

public class Block {

    private byte[] hash;
    private byte[] previousBlockHash;
    private List<Transaction> transactions;
    private byte[] merkleRoot;
    private long nonce;
    private long timestamp;

    public byte[] getHash() {
        return hash;
    }

    public Block setHash(byte[] hash) {
        this.hash = hash;
        return this;
    }

    public byte[] getPreviousBlockHash() {
        return previousBlockHash;
    }

    public Block setPreviousBlockHash(byte[] previousBlockHash) {
        this.previousBlockHash = previousBlockHash;
        return this;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public Block setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
        return this;
    }

    public byte[] getMerkleRoot() {
        return merkleRoot;
    }

    public Block setMerkleRoot(byte[] merkleRoot) {
        this.merkleRoot = merkleRoot;
        return this;
    }

    public long getNonce() {
        return nonce;
    }

    public Block setNonce(long nonce) {
        this.nonce = nonce;
        return this;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Block setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Block block = (Block) o;

        return Arrays.equals(hash, block.hash);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(hash);
    }
}
