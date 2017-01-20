package de.neozo.domain;


import java.util.Arrays;

public class Transaction {

    private byte[] hash;
    private String text;
    private byte[] senderHash;
    private byte[] signature;
    private long timestamp;

    public byte[] getHash() {
        return hash;
    }

    public Transaction setHash(byte[] hash) {
        this.hash = hash;
        return this;
    }

    public String getText() {
        return text;
    }

    public Transaction setText(String text) {
        this.text = text;
        return this;
    }

    public byte[] getSenderHash() {
        return senderHash;
    }

    public Transaction setSenderHash(byte[] senderHash) {
        this.senderHash = senderHash;
        return this;
    }

    public byte[] getSignature() {
        return signature;
    }

    public Transaction setSignature(byte[] signature) {
        this.signature = signature;
        return this;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Transaction setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;

        return Arrays.equals(hash, that.hash);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(hash);
    }
}
