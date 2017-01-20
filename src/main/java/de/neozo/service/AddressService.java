package de.neozo.service;


import de.neozo.domain.Address;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@Service
public class AddressService {

    private Map<byte[], Address> addresses = new HashMap<>();

    public Address getAddress(byte[] hash) {
        return addresses.get(hash);
    }

    public Collection<Address> getAddresses() {
        return addresses.values();
    }

    public void add(Address address) {
        addresses.put(address.getHash(), address);
    }

}
