package guru.springframework.spring6restmvc.controller;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class BeerControllerTest {

    @Autowired
    private BeerController beerController;

    @Test
    void testGetBeerByID() {
        log.debug("Test getBeerByID");
        System.out.println(beerController.getBeerByID(UUID.randomUUID()));
    }
}
