package guru.springframework.spring6restmvc.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping
    public ResponseEntity<Void> handlePost(@RequestBody Customer customer) {

        Customer savedCustomer = customerService.saveNewCustomer(customer);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/customer/" + savedCustomer.getId().toString());

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
}
