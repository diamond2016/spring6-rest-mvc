package guru.springframework.spring6restmvc.repositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import guru.springframework.spring6restmvc.entities.Customer;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;
    @Test
    void testSaveAndFindCustomer() {
    Customer customer = Customer.builder()
        .customerName("John Doe")
        .build();

        Customer savedCustomer = customerRepository.save(customer);

        Optional<Customer> found = customerRepository.findById(savedCustomer.getId());
        assertThat(found).isPresent();
    assertThat(found.get().getCustomerName()).isEqualTo("John Doe");
    }

    @Test
    void testDeleteCustomer() {
    Customer customer = Customer.builder()
        .customerName("Jane Smith")
        .build();

        Customer savedCustomer = customerRepository.save(customer);
        UUID id = savedCustomer.getId();

        customerRepository.deleteById(id);

        Optional<Customer> found = customerRepository.findById(id);
        assertThat(found).isNotPresent();
    }

    @Test
    void testFindAllCustomers() {
    Customer customer1 = Customer.builder().customerName("Alice").build();
    Customer customer2 = Customer.builder().customerName("Bob").build();

        customerRepository.save(customer1);
        customerRepository.save(customer2);

        assertThat(customerRepository.findAll())
                .extracting(Customer::getCustomerName)
                .contains("Alice", "Bob");
    }
}