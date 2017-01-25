package de.neozo.blockchain.node.service;


import de.neozo.blockchain.common.domain.Address;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@Service
public class AddressService {

    private Map<String, Address> addresses = new HashMap<>();

    public Address getByHash(byte[] hash) {
        return addresses.get(Base64.encodeBase64String(hash));
    }

    public Collection<Address> getAll() {
        return addresses.values();
    }

    public synchronized void add(Address address) {
        addresses.put(Base64.encodeBase64String(address.getHash()), address);
    }

}
