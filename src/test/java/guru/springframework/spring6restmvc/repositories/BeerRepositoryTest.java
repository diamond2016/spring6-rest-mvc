package guru.springframework.spring6restmvc.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.model.BeerStyle;
import jakarta.validation.ConstraintViolationException;

@DataJpaTest
public class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testSaveNewBeer() {
        Beer beer = Beer.builder()
                .beerName("Test Beer")
                .beerStyle(BeerStyle.IPA)
                .upc("123456789012")
                .price(BigDecimal.valueOf(12.3))
                .quantityOnHand(200)
                .build();

        Beer savedBeer = beerRepository.save(beer);
        beerRepository.flush(); // necessary otherwise Hybernate uses cache

        assertThat(savedBeer.getId()).isNotNull();
        assertThat(savedBeer.getBeerName().equals("Test Beer"));
    }

    @Test
    void testSaveNewBeerNullName() {
        Beer beer = Beer.builder()
            .beerStyle(BeerStyle.IPA)
            .upc("123456789012")
            .price(BigDecimal.valueOf(12.3))
            .quantityOnHand(200)
            .build();

        assertThrows(ConstraintViolationException.class, () -> {
            beerRepository.save(beer);
            beerRepository.flush();
        });
    }

    @Test
    void testSaveNewBeerTooLongName() {
        Beer beer = Beer.builder()
                .beerName("1234567890asbvdgrtgh1234567890asbvdgrtgh1234567890asbvdgrtgh")
                .beerStyle(BeerStyle.IPA)
                .upc("123456789012")
                .price(BigDecimal.valueOf(12.3))
                .quantityOnHand(200)
                .build();
        assertThrows(ConstraintViolationException.class, () -> {
            beerRepository.save(beer);
        // assertThrows(DataIntegrityViolationException.class, () -> { beerRepository.flush(); });  this is without @Size on entity
            beerRepository.flush(); });

    }

    @Test
    void testFindById() {
        Beer beer = Beer.builder()
                .beerName("FindMe")
                .upc("987654321098")
                .price(BigDecimal.valueOf(10.3))
                .quantityOnHand(100)
                .build();

        Beer savedBeer = beerRepository.save(beer);

        var foundBeer = beerRepository.findById(savedBeer.getId());

        assertThat(foundBeer.isPresent());
        assertThat(foundBeer.get().getBeerName().equals("FindMe"));
    }

    @Test
    void testDeleteBeer() {
        Beer beer = Beer.builder()
                .beerName("DeleteMe")
                .upc("111222333444")
                .price(BigDecimal.valueOf(8.0))
                .quantityOnHand(500)
                .build();

        Beer savedBeer = beerRepository.save(beer);
        beerRepository.delete(savedBeer);

        var foundBeer = beerRepository.findById(savedBeer.getId());

        assertThat(foundBeer.isEmpty());
    }

    @Test
    void testFindAll() {
        Beer beer1 = Beer.builder().beerName("Beer1").upc("000111222333").build();
        Beer beer2 = Beer.builder().beerName("Beer2").upc("444555666777").build();

        beerRepository.save(beer1);
        beerRepository.save(beer2);

        var beers = beerRepository.findAll();

        assertThat(beers.size() >= 2);
    }

    @Test
    void testFindByUpc() {
        String upc = "555666777888";
        Beer beer = Beer.builder()
                .beerName("FindMe")
                .upc(upc)
                .beerStyle(BeerStyle.IPA)
                .price(BigDecimal.valueOf(12.3))
                .quantityOnHand(200)
                .build();

        Beer savedBeer = beerRepository.save(beer);

        var foundBeer = beerRepository.findByUpc(upc);

        assertThat(foundBeer).isNotNull();
        assertThat(foundBeer.getBeerName().equals("FindMe"));
        assertThat(foundBeer.getUpc().equals(savedBeer.getUpc()));
    }
}
