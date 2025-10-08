package guru.springframework.spring6restmvc.controller;

import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import guru.springframework.spring6restmvc.model.Customer;
import guru.springframework.spring6restmvc.services.CustomerService;
import guru.springframework.spring6restmvc.services.CustomerServiceImpl;
import lombok.extern.slf4j.Slf4j;

@Slf4j
//@SpringBootTest
@WebMvcTest(CustomerController.class) 
public class CustomerControllerTest {
    //@Autowired
    //private CustomerController customerController;
    @Autowired
    MockMvc mockMvc;

    @MockitoBean  
    CustomerService customerService;

    CustomerServiceImpl customerServiceImpl;

    @Autowired
    ObjectMapper objectMapper;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;
    @Captor
    ArgumentCaptor<Customer> customerArgumentCaptor;

    @BeforeEach
    void setUp() {
        customerServiceImpl = new CustomerServiceImpl();
    }

    @Test
    void getCustomerByIdNotFound() throws Exception {

        given(customerService.getCustomerById(any(UUID.class))).willReturn(Optional.empty());

        mockMvc.perform(get(CustomerController.CUSTOMER_PATH_ID, UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    @Test
    void listCustomersTest() throws Exception {
        log.debug("test listCustomers ");
        given(customerService.listCustomers()).willReturn(customerServiceImpl.listCustomers());

        mockMvc.perform(get(CustomerController.CUSTOMER_PATH))
         //   .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()", is(3)));
        //List<Customer> myList = customerController.listCustomers();
        //System.out.println(myList);
        //assertEquals(myList.size(), 3);
    }

    @Test
    void getCustomerByIdTest() throws Exception {
        log.debug("test getCustomerById ");
        Customer testCustomer = customerServiceImpl.listCustomers().get(0);      

        given(customerService.getCustomerById(testCustomer.getId())).willReturn(Optional.of(testCustomer));

        mockMvc.perform(get(CustomerController.CUSTOMER_PATH_ID, testCustomer.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(testCustomer.getId().toString())))
            .andExpect(jsonPath("$.customerName", is(testCustomer.getCustomerName())));

        //List<Customer> myList = customerController.listCustomers();
        //UUID id = myList.get(0).getId();
        //System.out.println(id.toString());
        //System.out.println(customerController.getCustomerById(id));
        //assertEquals(myList.size(), 3);
    }

    @Test
    void testCreateNewCustomer() throws Exception {
        Customer customerToSave = customerServiceImpl.listCustomers().get(0);
        customerToSave.setId(null);
        customerToSave.setVersion(1);
        log.debug("test createNewCustomer ");
        Customer savedCustomer = Customer.builder()
                .id(UUID.randomUUID())
                .customerName("New Customer")
                .version(customerToSave.getVersion())
                .build();

        given(customerService.saveNewCustomer(any(Customer.class))).willReturn(savedCustomer);
        mockMvc.perform(post(CustomerController.CUSTOMER_PATH)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(customerToSave)))  // context of the request
            .andExpect(status().isCreated())                        // then
            .andExpect(header().string("Location", CustomerController.CUSTOMER_PATH + "/" + savedCustomer.getId())); // check exact url of location url+id
    }

    @Test
    void testUpdateCustomer() throws Exception {
        Customer customerToUpdate = customerServiceImpl.listCustomers().get(0);
        mockMvc.perform(put(CustomerController.CUSTOMER_PATH_ID, customerToUpdate.getId())
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(customerToUpdate)))
            .andExpect(status().isNoContent());
        
        verify(customerService).updateCustomerById(any(UUID.class), any(Customer.class)); // verify mockito mock controller is called
    }

    @Test 
    void testDeleteCustomer() throws Exception {
        Customer customerToDelete = customerServiceImpl.listCustomers().get(0);
        mockMvc.perform(delete(CustomerController.CUSTOMER_PATH_ID, customerToDelete.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        verify(customerService).deleteCustomerById(uuidArgumentCaptor.capture());
        assertThat(customerToDelete.getId()).isEqualTo(uuidArgumentCaptor.getValue()); // parameter passed in call to service check
    }

    @Test
    void testPatchCustomerById() throws Exception {
        Customer customerToPatch = customerServiceImpl.listCustomers().get(0);
        Map<String, Object> customerMap = new HashMap<>();
        customerMap.put("customerName", "newName");

        mockMvc.perform(patch(CustomerController.CUSTOMER_PATH_ID, customerToPatch.getId())
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerMap)))
            .andExpect(status().isNoContent());
        
        verify(customerService).patchCustomerById(uuidArgumentCaptor.capture(), customerArgumentCaptor.capture());
        assertThat(customerToPatch.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(customerMap.get("customerName")).isEqualTo(customerArgumentCaptor.getValue().getCustomerName());
    } 

}
