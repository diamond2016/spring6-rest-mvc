package guru.springframework.spring6restmvc.bootstrap;

import java.math.BigDecimal;
import java.time.LocalDateTime; 
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.entities.Customer;
import guru.springframework.spring6restmvc.mappers.BeerMapper;
import guru.springframework.spring6restmvc.mappers.CustomerMapper;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.model.BeerStyle;
import guru.springframework.spring6restmvc.model.CustomerDTO; 
import guru.springframework.spring6restmvc.repositories.BeerRepository;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class BootstrapData implements CommandLineRunner {

    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;
    private final BeerMapper beerMapper;
    private final CustomerMapper customerMapper;
  
 
    @Override
    public void run(String... args) throws Exception {
        loadBeerData();
        loadCustomerData();
    }

    private void loadBeerData() {
        if (beerRepository.count() == 0) {
            BeerDTO morettiDto = BeerDTO.builder()
                .beerName("Birra Moretti")
                .beerStyle(BeerStyle.LAGER)
                .upc("12345")
                .price(BigDecimal.valueOf(5.20))
                .quantityOnHand(100)
                .createdDate(LocalDateTime.now()) 
                .lastModifiedDate(LocalDateTime.now())
                .build();

            BeerDTO villacherDto = BeerDTO.builder()
                .beerName("Villacher Beer")
                .beerStyle(BeerStyle.PILSNER)
                .upc("1234555")
                .price(BigDecimal.valueOf(6.10))
                .quantityOnHand(300)
                .createdDate(LocalDateTime.now()) 
                .lastModifiedDate(LocalDateTime.now())
                .build();

            beerRepository.save(beerMapper.beerDtoToBeer(morettiDto));
            beerRepository.save(beerMapper.beerDtoToBeer(villacherDto));

            log.info("Loaded Beer Data. Beer count: " + beerRepository.count());
        }
    }

    private void loadCustomerData() {
        if (customerRepository.count() == 0) {
            CustomerDTO allItalianDto = CustomerDTO.builder()
                    .customerName("All Italian beer distribution")
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .build();

            CustomerDTO villacherRestaurantDto = CustomerDTO.builder()
                    .customerName("Villacher typical restaurant")
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .build();

            customerRepository.save(customerMapper.customerDtoToCustomer(allItalianDto));
            customerRepository.save(customerMapper.customerDtoToCustomer(villacherRestaurantDto));

            log.info("Loaded Customer Data. Customer count: " + customerRepository.count());
        }
    }

}
