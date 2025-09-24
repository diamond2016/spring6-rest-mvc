package guru.springframework.spring6restmvc.controller;

import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import guru.springframework.spring6restmvc.model.Beer;
import guru.springframework.spring6restmvc.model.Customer;
import guru.springframework.spring6restmvc.services.BeerService;
import guru.springframework.spring6restmvc.services.BeerServiceImpl;
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

    @BeforeEach
    void setUp() {
        customerServiceImpl = new CustomerServiceImpl();
    }

    @Test
    void listCustomersTest() throws Exception {
        log.debug("test listCustomers ");
        given(customerService.listCustomers()).willReturn(customerServiceImpl.listCustomers());

        mockMvc.perform(get("/api/v1/customer"))
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

        given(customerService.getCustomerById(testCustomer.getId())).willReturn(testCustomer);

        mockMvc.perform(get("/api/v1/customer/" + testCustomer.getId())
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
        mockMvc.perform(post("/api/v1/customer")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(customerToSave)))  // context of the request
            .andExpect(status().isCreated())                        // then
            .andExpect(header().string("Location", "/api/v1/customer/" + savedCustomer.getId())); // check exact url of location url+id
    }

}
