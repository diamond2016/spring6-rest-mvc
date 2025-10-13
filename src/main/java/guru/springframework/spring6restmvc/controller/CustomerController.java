package guru.springframework.spring6restmvc.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.services.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RestController

public class CustomerController {
    public static final String CUSTOMER_PATH = "/api/v1/customer";
    public static final String CUSTOMER_PATH_ID = CUSTOMER_PATH + "/{customerId}";
    private final CustomerService customerService;

    @GetMapping(CUSTOMER_PATH_ID)
    // customerId is also a path variable in requests
    public CustomerDTO getCustomerById(@PathVariable("customerId") UUID customerId) {

        log.debug("get Customer from ID - in controller id: {}", customerId);
        
        return customerService.getCustomerById(customerId).orElseThrow(NotFoundException::new);
    }

    @GetMapping(CUSTOMER_PATH)
    public List<CustomerDTO> listCustomers() {

        log.debug("List Customers - in controller ");
        
        return customerService.listCustomers();
    }

    @PostMapping(CUSTOMER_PATH)
    public ResponseEntity<Void> handlePost(@RequestBody CustomerDTO customer) {

        CustomerDTO savedCustomer = customerService.saveNewCustomer(customer);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/customer/" + savedCustomer.getId().toString());

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping(CUSTOMER_PATH_ID)
    public ResponseEntity<Void> updateCustomerById(@PathVariable("customerId") UUID customerId, 
                                @RequestBody CustomerDTO customer) {
        customerService.updateCustomerById(customerId, customer);
        log.debug("update Customer of Id - in controller id: {}", customerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(CUSTOMER_PATH_ID)
    public ResponseEntity<Void> deleteCustomerById(@PathVariable("customerId") UUID customerId) {
        customerService.deleteCustomerById(customerId);
        log.debug("delete Customer of Id - in controller id: {}", customerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @PatchMapping(CUSTOMER_PATH_ID)
    public ResponseEntity<Void> updateCustomerPatchById(@PathVariable("customerId") UUID customerId, 
                                                        @RequestBody CustomerDTO customer) {
        customerService.patchCustomerById(customerId, customer);
        log.debug("update Customer patch of ID - in controller id: {}", customerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
