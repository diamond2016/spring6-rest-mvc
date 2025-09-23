package guru.springframework.spring6restmvc.controller;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import guru.springframework.spring6restmvc.model.Beer;
import guru.springframework.spring6restmvc.services.BeerService;
import guru.springframework.spring6restmvc.services.BeerServiceImpl;
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

    BeerServiceImpl beerServiceImpl = new BeerServiceImpl();

    @Test
    void testGetBeerById() throws Exception {
        log.debug("Test via Mockito and MockMvc getBeerByID");
        // System.out.println(beerController.getBeerById(beers.getFirst().getId()));
        Beer testBeer = beerServiceImpl.listBeers().get(0);
        
        given(beerService.getBeerById(any(UUID.class))).willReturn(testBeer);
        
        mockMvc.perform(get("/api/v1/beer/" + UUID.randomUUID())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())     // 200
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(testBeer.getId().toString())))
            .andExpect(jsonPath("$.beerName", is(testBeer.getBeerName())));
    }

    @Test
    void testListBeers() throws Exception {
        log.debug("Test listBeers");
        given(beerService.listBeers()).willReturn(beerServiceImpl.listBeers());

        mockMvc.perform(get("/api/v1/beer").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()", is(3)));
    }
}
