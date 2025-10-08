package guru.springframework.spring6restmvc.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import guru.springframework.spring6restmvc.model.Customer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {
    private Map<UUID, Customer> customerMap = new HashMap<>();

    public CustomerServiceImpl() {
        Customer cust1 = Customer.builder()
                .id(UUID.randomUUID())
                .customerName("ABC beer")
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

                Customer cust2 = Customer.builder()
                .id(UUID.randomUUID())
                .customerName("XGH bar Springfield")
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

                Customer cust3 = Customer.builder()
                .id(UUID.randomUUID())
                .customerName("The Guild beer distributor")
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        customerMap.put(cust1.getId(), cust1);
        customerMap.put(cust2.getId(), cust2);
        customerMap.put(cust3.getId(), cust3);
    }

    @Override
    public List<Customer> listCustomers() {
        log.debug("listCustomer from service: ");

        return new ArrayList<>(customerMap.values());
    } 

    @Override
    public Optional<Customer> getCustomerById(UUID id) {
         log.debug("getCustomerById from service: id: " + id);
        
         return Optional.of(customerMap.get(id));
    }

    @Override
    public Customer saveNewCustomer(Customer customer) {
         log.debug("saved new Customer, id: {}", customer.getId());
        
         Customer savedCustomer = Customer.builder()
                .id(UUID.randomUUID())
                .customerName(customer.getCustomerName())
                .version(customer.getVersion())
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        customerMap.put(savedCustomer.getId(), savedCustomer);
        log.debug("saved new Customer, id: {}", customer.getId());
        return savedCustomer;
    }

    @Override
    public void updateCustomerById(UUID customerId, Customer customer) {

        Customer existingCustomer = customerMap.get(customerId);
        if (existingCustomer == null)
            return;

        existingCustomer.setCustomerName(customer.getCustomerName());
        existingCustomer.setVersion(customer.getVersion());
        existingCustomer.setCreatedDate(customer.getCreatedDate());
        existingCustomer.setLastModifiedDate(LocalDateTime.now());

        customerMap.put(existingCustomer.getId(), existingCustomer);
        log.debug("update Customer, id: {}", customer.getId());
        return;
    }

    @Override
    public void deleteCustomerById(UUID customerId) {

        Customer existingCustomer = customerMap.get(customerId);
        if (existingCustomer == null)
            return;
        
        customerMap.remove(customerId);
        log.debug("delete Customer, id: {}", customerId);
    }

    @Override
    public void patchCustomerById(UUID customerId, Customer customer) {
        Customer existingCustomer = customerMap.get(customerId);
        if (existingCustomer == null)
            return;

        if (StringUtils.hasText(customer.getCustomerName())) {
            existingCustomer.setCustomerName(customer.getCustomerName());
        }

        if (customer.getVersion() != null) {
            existingCustomer.setVersion(customer.getVersion());
        }

        if (customer.getCreatedDate() != null) {
            existingCustomer.setCreatedDate(customer.getCreatedDate());
        }
        if (customer.getLastModifiedDate() != null) {
            existingCustomer.setLastModifiedDate(customer.getLastModifiedDate());
        }

    }
}

