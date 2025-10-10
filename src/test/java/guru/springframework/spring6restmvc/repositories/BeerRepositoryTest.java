package guru.springframework.spring6restmvc.repositories;

import guru.springframework.spring6restmvc.entities.Beer;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testSaveNewBeer() {
        Beer beer = Beer.builder()
                .beerName("Test Beer")
                .upc("123456789012")
                .build();

        Beer savedBeer = beerRepository.save(beer);

        assertThat(savedBeer.getId()).isNotNull();
        assertThat(savedBeer.getBeerName().equals("Test Beer"));
    }

    @Test
    void testFindById() {
        Beer beer = Beer.builder()
                .beerName("FindMe")
                .upc("987654321098")
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
}
