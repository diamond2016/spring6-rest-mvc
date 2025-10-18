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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

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
    @Autowired
    WebApplicationContext wac;

    MockMvc mockMvc;

    void setUp() {
      mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();  // for validations in repositories
    }

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

    @Test
    void updateBeerByIdNotFound() throws Exception {

        UUID notFoundId = UUID.randomUUID();
        BeerDTO beerDTOToUpdate = BeerDTO.builder().build();
        assertThrows(NotFoundException.class, () -> beerController.updateBeerById(notFoundId, beerDTOToUpdate));
        log.debug("update Beer by not found Id - in integration test. ramdom id ");
    }

    @Rollback
    @Transactional
    @Test
    void testDeleteBeerById() {
        Beer beerToDelete = beerRepository.findAll().get(0);

        ResponseEntity<Void> responseEntity = beerController.deleteBeerById(beerToDelete.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204)); // 201 = NO CONTENT
        assertThrows(NotFoundException.class, () -> beerController.getBeerById(beerToDelete.getId()));

    }

    @Test
    void deleteBeerByIdNotFound() throws Exception {

        UUID notFoundId = UUID.randomUUID();
        assertThrows(NotFoundException.class, () -> beerController.deleteBeerById(notFoundId));
        log.debug("delete Beer by not found Id - in integration test. ramdom id ");
    }

    @Rollback
    @Transactional
    @Test
    void testPatchBeerById() {
        Beer beerToPatch = beerRepository.findAll().get(0);
        BeerDTO beerDTOToPatch = beerMapper.beerToBeerDTO(beerToPatch);
        beerDTOToPatch.setId(null);
        beerDTOToPatch.setVersion(null);
        String newName = beerToPatch.getBeerName() + "updated";
        beerDTOToPatch.setBeerName(newName);
        
        ResponseEntity<Void> responseEntity = beerController.patchBeerById(beerToPatch.getId(), beerDTOToPatch);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204)); // 201 = NO CONTENT

        Beer patchBeer = beerRepository.findById(beerToPatch.getId()).get();
        assertThat(patchBeer.getBeerName()).isEqualTo(newName);
    }

    @Test
    void testPatchBeerByIdNotFound() {
        UUID notFoundId = UUID.randomUUID();
        BeerDTO beerDTOToPatch = BeerDTO.builder().build();
        assertThrows(NotFoundException.class, () -> beerController.patchBeerById(notFoundId, beerDTOToPatch));
        log.debug("patch Beer by not found Id - in integration test. ramdom id ");
    }

    @Test
    void testUpdateBeerBlankName() throws Exception {
        BeerDTO beerDTOToUpdate = beerMapper.beerToBeerDTO(beerRepository.findAll().get(0));
        beerDTOToUpdate.setBeerName("");

        assertThrows(TransactionSystemException.class, () -> 
            { beerController.updateBeerById(beerDTOToUpdate.getId(), beerDTOToUpdate); });           
    
    /* Spring returns one or more fieldError with structure like this;        [
    {
        "field": "beerName",
        "defaultMessage": "must not be blank"
    }
    at controller level Validation error is tranformed ad Transaction error
    */
    }
}