package de.neozo.domain;

import java.util.Arrays;

public class Address {

    private byte[] hash;
    private String name;
    private byte[] publicKey;

    public byte[] getHash() {
        return hash;
    }

    public Address setHash(byte[] hash) {
        this.hash = hash;
        return this;
    }

    public byte[] getPublicKey() {
        return publicKey;
    }

    public Address setPublicKey(byte[] publicKey) {
        this.publicKey = publicKey;
        return this;
    }

    public String getName() {
        return name;
    }

    public Address setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;

        return Arrays.equals(hash, address.hash);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(hash);
    }
}
