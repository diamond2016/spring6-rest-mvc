package guru.springframework.spring6restmvc.services;

import java.util.List;
import java.util.UUID;

import guru.springframework.spring6restmvc.model.Customer;

public interface CustomerService {

    public List<Customer> listCustomers(); 
    
    public Customer getCustomerById(UUID id);

    public Customer saveNewCustomer(Customer customer);

}
