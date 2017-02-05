package de.neozo.jblockchain.common.domain;


import com.google.common.primitives.Longs;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

public class Block {

    @NotNull
    private byte[] hash;

    private byte[] previousBlockHash;

    @NotNull
    private List<Transaction> transactions;

    @NotNull
    private byte[] merkleRoot;

    private long nonce;

    private long timestamp;

    public Block() {
    }

    public Block(byte[] previousBlockHash, List<Transaction> transactions, long nonce) {
        this.previousBlockHash = previousBlockHash;
        this.transactions = transactions;
        this.nonce = nonce;
        this.timestamp = System.currentTimeMillis();
        this.merkleRoot = calculateMerkleRoot();
        this.hash = calculateHash();
    }

    public byte[] getHash() {
        return hash;
    }

    public void setHash(byte[] hash) {
        this.hash = hash;
    }

    public byte[] getPreviousBlockHash() {
        return previousBlockHash;
    }

    public void setPreviousBlockHash(byte[] previousBlockHash) {
        this.previousBlockHash = previousBlockHash;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public byte[] getMerkleRoot() {
        return merkleRoot;
    }

    public void setMerkleRoot(byte[] merkleRoot) {
        this.merkleRoot = merkleRoot;
    }

    public long getNonce() {
        return nonce;
    }

    public void setNonce(long nonce) {
        this.nonce = nonce;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public byte[] calculateHash() {
        byte[] hashableData = ArrayUtils.addAll(previousBlockHash, merkleRoot);
        hashableData = ArrayUtils.addAll(hashableData, Longs.toByteArray(nonce));
        hashableData = ArrayUtils.addAll(hashableData, Longs.toByteArray(timestamp));
        return DigestUtils.sha256(hashableData);
    }

    public byte[] calculateMerkleRoot() {
        Queue<byte[]> hashQueue = new LinkedList<>();
        hashQueue.addAll(transactions.stream().map(Transaction::getHash).collect(Collectors.toList()));
        while (hashQueue.size() > 1) {
            byte[] hashableData = ArrayUtils.addAll(hashQueue.poll(), hashQueue.poll());
            hashQueue.add(DigestUtils.sha256(hashableData));
        }
        return hashQueue.poll();
    }

    public int getLeadingZerosCount() {
        for (int i = 0; i < getHash().length; i++) {
            if (getHash()[i] != 0) {
                return i;
            }
        }
        return getHash().length;
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
