package de.neozo.rest;


import de.neozo.domain.Address;
import de.neozo.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;


@RestController()
@RequestMapping("address")
public class AddressController {

    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @RequestMapping
    Collection<Address> getTransactionPool() {
        return addressService.getAddresses();
    }


    @RequestMapping(method = RequestMethod.PUT)
    void addAddress(@RequestBody Address address) {
        addressService.add(address);
    }

    @RequestMapping(path = "/publish", method = RequestMethod.POST)
    void publishAddress(@RequestBody Address address) {
        // broadcast transaction to all nodes
    }

}
