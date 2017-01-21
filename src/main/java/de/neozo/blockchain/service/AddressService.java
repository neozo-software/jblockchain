package de.neozo.blockchain.service;


import de.neozo.blockchain.domain.Address;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@Service
public class AddressService {

    private Map<byte[], Address> addresses = new HashMap<>();

    public Address getByHash(byte[] hash) {
        return addresses.get(hash);
    }

    public Collection<Address> getAll() {
        return addresses.values();
    }

    public synchronized void add(Address address) {
        addresses.put(address.getHash(), address);
    }

}
