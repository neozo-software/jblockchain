package de.neozo.blockchain.common.domain;


import com.google.common.primitives.Longs;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;

import javax.validation.constraints.NotNull;
import java.util.Arrays;

public class Transaction {

    @NotNull
    private byte[] hash;

    @NotNull
    private String text;

    @NotNull
    private byte[] senderHash;

    @NotNull
    private byte[] signature;

    private long timestamp;

    public Transaction() {
    }

    public Transaction(String text, byte[] senderHash, byte[] signature) {
        this.text = text;
        this.senderHash = senderHash;
        this.signature = signature;
        this.timestamp = System.currentTimeMillis();
        this.hash = calculateHash();
    }

    public byte[] getHash() {
        return hash;
    }

    public void setHash(byte[] hash) {
        this.hash = hash;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public byte[] getSenderHash() {
        return senderHash;
    }

    public void setSenderHash(byte[] senderHash) {
        this.senderHash = senderHash;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public byte[] getSignableData() {
        return text.getBytes();
    }

    public byte[] calculateHash() {
        byte[] hashableData = ArrayUtils.addAll(text.getBytes(), senderHash);
        hashableData = ArrayUtils.addAll(hashableData, signature);
        hashableData = ArrayUtils.addAll(hashableData, Longs.toByteArray(timestamp));
        return DigestUtils.sha256(hashableData);
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
