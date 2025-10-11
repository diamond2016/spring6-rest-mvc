package guru.springframework.spring6restmvc.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class CustomerControllerIntegrationTest {
    
    @Autowired
    private CustomerController customerController;
    @Autowired
    private CustomerRepository customerRepository;
    
    @Test
    void testListCustomers() {
        List<CustomerDTO> dtos = customerController.listCustomers();
        assertThat(dtos.size()).isEqualTo(2);
    }
    
    @Rollback
    @Transactional
    @Test
    void testEmptyListCustomers() {
        customerRepository.deleteAll();
        List<CustomerDTO> dtos = customerController.listCustomers();
        assertThat(dtos.size()).isZero();
    }
    
    @Test
    void testGetCustomerById() {
        List<CustomerDTO> dtos = customerController.listCustomers();
        UUID foundId = dtos.get(0).getId();
        CustomerDTO dto = customerController.getCustomerById(foundId);
        assertThat(dto).isNotNull();
        assertThat(dto.getCustomerName()).isEqualTo(dtos.get(0).getCustomerName());
    }
    
    @Test
    void testGetCustomerByNotFoundId() {
        UUID notFoundId = UUID.randomUUID();
        assertThrows(NotFoundException.class, () -> customerController.getCustomerById(notFoundId));
    }
}
