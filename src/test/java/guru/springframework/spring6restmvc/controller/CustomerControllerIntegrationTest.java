package guru.springframework.spring6restmvc.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import guru.springframework.spring6restmvc.entities.Customer;
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

    @Rollback
    @Transactional
    @Test
    void testUpdateCustomerById() {
        UUID customerDtoIdToUpdate = customerRepository.findAll().get(0).getId();
        CustomerDTO customerDTOToUpdate = CustomerDTO.builder().build();
        customerDTOToUpdate.setId(null);
        customerDTOToUpdate.setVersion(null);
        String newName = "updated";
        customerDTOToUpdate.setCustomerName(newName);
        
        ResponseEntity<Void> responseEntity = customerController.updateCustomerById(customerDtoIdToUpdate, customerDTOToUpdate);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204)); // 204 = NO CONTENT

        Customer updateCustomer = customerRepository.findById(customerDtoIdToUpdate).get();
        assertThat(updateCustomer.getCustomerName()).isEqualTo(newName);
        log.debug("update Customer by found Id - in integration test. id " + customerDtoIdToUpdate); 
    }

    @Test
    void testUpdateCustomerByNotFoud() {

        UUID notFoundId = UUID.randomUUID();
        CustomerDTO customerDTOToUpdate = CustomerDTO.builder().build();
        assertThrows(NotFoundException.class, () -> customerController.updateCustomerById(notFoundId, customerDTOToUpdate));
        log.debug("update Customer by not found Id - in integration test. ramdom id ");
    }

    @Rollback
    @Transactional
    @Test
    void testDeleteCustomerById() {

        UUID customerDtoIdToDelete = customerRepository.findAll().get(0).getId();
  
        ResponseEntity<Void> responseEntity = customerController.deleteCustomerById(customerDtoIdToDelete);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204)); // 204 = NO CONTENT
        assertThrows(NotFoundException.class, () -> customerController.getCustomerById(customerDtoIdToDelete));
        log.debug("update Customer by found Id - in integration test. id " + customerDtoIdToDelete);
    }

    @Test
    void testDeleteCustomerByIdNotFound() {
        UUID notFoundId = UUID.randomUUID();

        assertThrows(NotFoundException.class, () -> customerController.deleteCustomerById(notFoundId));
        log.debug("update Customer by not found Id - in integration test. ramdom id ");
    }

    @Rollback
    @Transactional
    @Test
    void testSaveNewCustomer() {
        CustomerDTO customerDTO = CustomerDTO.builder()
            .customerName("New Customer to insert")
            .createdDate(LocalDateTime.now())
            .lastModifiedDate(LocalDateTime.now())
            .build();

        ResponseEntity<Void> responseEntity = customerController.handlePost(customerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201)); // 201 = CREATED
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();
        
        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(locationUUID[locationUUID.length -1]);
        Customer savedCustomer = customerRepository.findById(savedUUID).get(); // optional
        assertThat(savedCustomer).isNotNull();

    }
}

