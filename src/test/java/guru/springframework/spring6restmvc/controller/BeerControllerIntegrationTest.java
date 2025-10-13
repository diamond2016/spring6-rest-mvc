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

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.mappers.BeerMapper;
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
    @Autowired
    BeerMapper beerMapper;

    @Test
    void testGetBeerById() {
        List<BeerDTO> dtos = beerController.listBeers();
        UUID foundId = dtos.get(0).getId();
        BeerDTO dto = beerController.getBeerById(foundId);
        assertThat(dto).isNotNull();
        assertThat(dto.getBeerName()).isEqualTo(dtos.get(0).getBeerName());
        log.debug("get Beer by Id - in integration test. name: " + dto.getBeerName());

    }
    @Test
    void testGetBeerByIdNotFound() {
        UUID notFoundId = UUID.randomUUID();
        assertThrows(NotFoundException.class, () -> beerController.getBeerById(notFoundId));
        log.debug("get Beer by not found Id - in integration test. ramdom id ");

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

    @Rollback
    @Transactional
    @Test
    void testSaveNewBeer() {
        BeerDTO beerDTO = BeerDTO.builder()
            .beerName("New Beer to insert")
            .createdDate(LocalDateTime.now())
            .lastModifiedDate(LocalDateTime.now())
            .build();

        ResponseEntity<Void> responseEntity = beerController.handlePost(beerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201)); // 201 = CREATED
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();
        
        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(locationUUID[locationUUID.length -1]);
        Beer savedBeer = beerRepository.findById(savedUUID).get(); // optional
        assertThat(savedBeer).isNotNull();

    }

    @Rollback
    @Transactional
    @Test
    void testUpdateBeerById() {
        Beer beerToUpdate = beerRepository.findAll().get(0);
        BeerDTO beerDTOToUpdate = beerMapper.beerToBeerDTO(beerToUpdate);
        beerDTOToUpdate.setId(null);
        beerDTOToUpdate.setVersion(null);
        String newName = beerToUpdate.getBeerName() + "updated";
        beerDTOToUpdate.setBeerName(newName);
        
        ResponseEntity<Void> responseEntity = beerController.updateBeerById(beerToUpdate.getId(), beerDTOToUpdate);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204)); // 201 = NO CONTENT

        Beer updateBeer = beerRepository.findById(beerToUpdate.getId()).get();
        assertThat(updateBeer.getBeerName()).isEqualTo(newName);

    }
}
