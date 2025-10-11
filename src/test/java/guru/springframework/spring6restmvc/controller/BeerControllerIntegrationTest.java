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

import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.repositories.BeerRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest // uses complete context of Spring, bootstrap data also
public class BeerControllerIntegrationTest {

    @Autowired
    BeerController beerController;
    @Autowired
    BeerRepository beerRepository;

    @Test
    void testGetBeerById() {
        UUID notFoundId = UUID.randomUUID();
        assertThrows(NotFoundException.class, () -> beerController.getBeerById(notFoundId));

        List<BeerDTO> dtos = beerController.listBeers();
        UUID foundId = dtos.get(0).getId();
        BeerDTO dto = beerController.getBeerById(foundId);
        assertThat(dto).isNotNull();
        assertThat(dto.getBeerName()).isEqualTo(dtos.get(0).getBeerName());
        log.debug("get Beer by Id - in integration test. name: " + dto.getBeerName());

    }

    @Test
    void testListBeers() {
        List<BeerDTO> dtos = beerController.listBeers();
        assertThat(dtos.size()).isEqualTo(2);
        log.debug("testListBeers - in integration test. Size: " + dtos.size());
    }

    @Rollback
    @Transactional
    @Test
    void testEmptyListBeers() {
        beerRepository.deleteAll();
        List<BeerDTO> dtos = beerController.listBeers();
        assertThat(dtos.size()).isEqualTo(0);
        log.debug("testEmptyListBeers - in integration test with no data. Size: " + dtos.size());
    }
}
