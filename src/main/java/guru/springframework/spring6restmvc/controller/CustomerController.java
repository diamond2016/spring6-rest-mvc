package guru.springframework.spring6restmvc.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import guru.springframework.spring6restmvc.model.Customer;
import guru.springframework.spring6restmvc.services.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/customer") //no final shash
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("{customerId}")
    // customerId is also a path variable in requests
    public Customer getCustomerById(@PathVariable("customerId") UUID customerId) {

        log.debug("get Customer from ID - in controller id: {}", customerId);
        
        return customerService.getCustomerById(customerId);
    }

    @GetMapping
    public List<Customer> listCustomers() {

        log.debug("List Customers - in controller ");
        
        return customerService.listCustomers();
    }
}
