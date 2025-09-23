package guru.springframework.spring6restmvc.controller;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import guru.springframework.spring6restmvc.services.BeerService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
// @SpringBootTest
@WebMvcTest(BeerController.class) // we say test only thiso controller if null prepares to test all controllers
public class BeerControllerTest {

    //@Autowired
    // private BeerController beerController;
    @Autowired
    MockMvc mockMvc;

    @MockitoBean  
    BeerService beerService; 

    @Test
    void testGetBeerByID() throws Exception {
        log.debug("Test via Mockito and MockMvc getBeerByID");
        // System.out.println(beerController.getBeerById(beers.getFirst().getId()));
        mockMvc.perform(get("/api/v1/beer/" + UUID.randomUUID())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    void testlistBeers() {
        log.debug("Test listBeers");
        //System.out.println(beerController.listBeers());
    }
}
