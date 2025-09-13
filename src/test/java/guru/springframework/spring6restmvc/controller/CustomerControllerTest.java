package guru.springframework.spring6restmvc.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import guru.springframework.spring6restmvc.model.Customer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class CustomerControllerTest {
    @Autowired
    private CustomerController customerController;
    
    @Test
    void listCustomersTest() {
        log.debug("test listCustomers ");

        List<Customer> myList = customerController.listCustomers();
        System.out.println(myList);

        assertEquals(myList.size(), 3);
    }

    @Test
    void getCustomerByIdTest() {
        log.debug("test getCustomerById ");

        List<Customer> myList = customerController.listCustomers();
        UUID id = myList.get(0).getId();
        System.out.println(id.toString());
        System.out.println(customerController.getCustomerById(id));

        assertEquals(myList.size(), 3);
    }



}
