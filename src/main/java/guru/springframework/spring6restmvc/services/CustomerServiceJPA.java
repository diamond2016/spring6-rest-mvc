package guru.springframework.spring6restmvc.services;

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
	public void updateCustomerById(UUID id, CustomerDTO customerDTO) {
		// TODO: Implement this method
	}

	@Override
	public void deleteCustomerById(UUID id) {
		// TODO: Implement this method
	}

	@Override
	public void patchCustomerById(UUID id, CustomerDTO customerDTO) {
		// TODO: Implement this method
	}
}
