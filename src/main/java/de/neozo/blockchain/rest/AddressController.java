package de.neozo.blockchain.rest;


import de.neozo.blockchain.domain.Address;
import de.neozo.blockchain.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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
        return addressService.getAll();
    }


    @RequestMapping(method = RequestMethod.PUT)
    void addAddress(@RequestBody Address address, @RequestParam(required = false) Boolean publish, HttpServletResponse response) {
        addressService.add(address);
    }

}
