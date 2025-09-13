package guru.springframework.spring6restmvc.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
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
    public Customer getCustomerById(UUID id) {
         log.debug("getCustomerById from service: id: " + id);
        
         return customerMap.get(id);
    }
}
