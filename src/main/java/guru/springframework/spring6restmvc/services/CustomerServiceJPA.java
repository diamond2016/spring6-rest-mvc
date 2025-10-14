package guru.springframework.spring6restmvc.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import guru.springframework.spring6restmvc.mappers.CustomerMapper;
import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;

@Service
@Primary
@RequiredArgsConstructor
public class CustomerServiceJPA implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public List<CustomerDTO> listCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customerMapper::customerToCustomerDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {
        return customerRepository.findById(id)
                .map(customerMapper::customerToCustomerDTO);
    }

    @Override
    public CustomerDTO saveNewCustomer(CustomerDTO customerDTO) {
        // TODO: Implement this method
        return null;
    }

    @Override
    public Optional<CustomerDTO> updateCustomerById(UUID id, CustomerDTO customerDTO) {
        return customerRepository.findById(id)
        .map(existingCustomer -> {    // map returns authomatically Optional.empty if not present 
            existingCustomer.setCustomerName(customerDTO.getCustomerName());
            existingCustomer.setLastModifiedDate(LocalDateTime.now());
            return customerMapper.customerToCustomerDTO(customerRepository.save(existingCustomer));
        });
    }

    @Override
    public Boolean deleteCustomerById(UUID id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return true;
        }
        
        return false;
    }

    @Override
    public void patchCustomerById(UUID id, CustomerDTO customerDTO) {
        // TODO: Implement this method
    }
}
