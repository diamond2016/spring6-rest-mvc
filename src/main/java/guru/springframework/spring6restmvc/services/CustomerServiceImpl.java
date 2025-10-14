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

import guru.springframework.spring6restmvc.model.CustomerDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {
    private Map<UUID, CustomerDTO> customerMap = new HashMap<>();

    public CustomerServiceImpl() {
        CustomerDTO cust1 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .customerName("ABC beer")
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

                CustomerDTO cust2 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .customerName("XGH bar Springfield")
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

                CustomerDTO cust3 = CustomerDTO.builder()
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
    public List<CustomerDTO> listCustomers() {
        log.debug("listCustomer from service: ");

        return new ArrayList<>(customerMap.values());
    } 

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {
         log.debug("getCustomerById from service: id: " + id);
        
         return Optional.ofNullable(customerMap.get(id));
    }

    @Override
    public CustomerDTO saveNewCustomer(CustomerDTO customer) {
         log.debug("saved new Customer, id: {}", customer.getId());
        
         CustomerDTO savedCustomer = CustomerDTO.builder()
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
    public Optional<CustomerDTO> updateCustomerById(UUID customerId, CustomerDTO customer) {

        CustomerDTO existingCustomer = customerMap.get(customerId);

        existingCustomer.setCustomerName(customer.getCustomerName());
        existingCustomer.setVersion(customer.getVersion());
        existingCustomer.setCreatedDate(customer.getCreatedDate());
        existingCustomer.setLastModifiedDate(LocalDateTime.now());

        customerMap.put(existingCustomer.getId(), existingCustomer);
        log.debug("update Customer, id: {}", customer.getId());
        return Optional.ofNullable(customerMap.get(customerId));
    }

    @Override
    public Boolean deleteCustomerById(UUID customerId) {

        CustomerDTO existingCustomer = customerMap.get(customerId);
        if (existingCustomer == null)
            return false;
        
        customerMap.remove(customerId);
        log.debug("delete Customer, id: {}", customerId);
        return true;
    }

    @Override
    public void patchCustomerById(UUID customerId, CustomerDTO customer) {
        CustomerDTO existingCustomer = customerMap.get(customerId);
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
