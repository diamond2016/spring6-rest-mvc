package guru.springframework.spring6restmvc.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.services.BeerService;
import guru.springframework.spring6restmvc.services.BeerServiceImpl;
import lombok.extern.slf4j.Slf4j;

@Slf4j
// @SpringBootTest
@WebMvcTest(BeerController.class) // we say: test only this controller class if not inserted will prepare to test all controllers
public class BeerControllerTest {

    //@Autowired
    // private BeerController beerController;
    @Autowired
    MockMvc mockMvc;

    @MockitoBean  
    BeerService beerService; 

    @Autowired
    ObjectMapper objectMapper; // serializza/deserializza json pojo <-> json

    BeerServiceImpl beerServiceImpl;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;
    @Captor
    ArgumentCaptor<BeerDTO> beerArgumentCaptor;

    @BeforeEach
    void setUp() {
        beerServiceImpl = new BeerServiceImpl(); // rigenera il servizio ogni volta
    }

    @Test
    void getBeerByIdNotFound() throws Exception {

        given(beerService.getBeerById(any(UUID.class))).willReturn(Optional.empty());

        mockMvc.perform(get(BeerController.BEER_PATH_ID, UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    @Test
    void testGetBeerById() throws Exception {
        log.debug("Test via Mockito and MockMvc getBeerByID");
        // System.out.println(beerController.getBeerById(beers.getFirst().getId()));
        BeerDTO testBeer = beerServiceImpl.listBeers().get(0);
        
        given(beerService.getBeerById(testBeer.getId())).willReturn(Optional.of(testBeer));
        
        mockMvc.perform(get(BeerController.BEER_PATH_ID, testBeer.getId())
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

        mockMvc.perform(get(BeerController.BEER_PATH)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))  // context of the response
            .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    void testCreateNewBeer() throws Exception {
        BeerDTO beerToSave = beerServiceImpl.listBeers().get(0);
        beerToSave.setId(null);
        beerToSave.setVersion(null);

        BeerDTO savedBeer = BeerDTO.builder()
                .id(UUID.randomUUID())
                .beerName("New Beer")
                .build();

        given(beerService.saveNewBeer(any(BeerDTO.class))).willReturn(savedBeer);
        
        mockMvc.perform(post(BeerController.BEER_PATH)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(beerToSave)))  // context of the request
            .andExpect(status().isCreated())                        // then
            .andExpect(header().string("Location", BeerController.BEER_PATH + "/" + savedBeer.getId())); // check exact url of location url+id
    }

    @Test
    void testUpdateBeer() throws Exception {
        BeerDTO beerToUpdate = beerServiceImpl.listBeers().get(0);
        
        given(beerService.updateBeerById(any(), any())).willReturn(Optional.of(beerToUpdate));
        // does not modify data but calls put starting from an Optional value not null
        mockMvc.perform(put(BeerController.BEER_PATH_ID, beerToUpdate.getId())
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(beerToUpdate)))
            .andExpect(status().isNoContent());
  
        verify(beerService).updateBeerById(any(UUID.class), any(BeerDTO.class)); // verify mockito mock can call service
    }

    @Test
    void testDeleteBeer() throws Exception {
        BeerDTO beerToDelete = beerServiceImpl.listBeers().get(0);

        given(beerService.deleteBeerById(any())).willReturn(true);
        // does not modify data but calls put starting from an Optional value not null

        mockMvc.perform(delete(BeerController.BEER_PATH_ID, beerToDelete.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        verify(beerService).deleteBeerById(uuidArgumentCaptor.capture());
        assertThat(beerToDelete.getId()).isEqualTo(uuidArgumentCaptor.getValue()); // parameter passed in call to service check 
    }

    @Test
    void  testPatchBeer() throws Exception {
        BeerDTO beerToPatch = beerServiceImpl.listBeers().get(0);
        Map<String, Object> beerMap = new HashMap<>();
        beerMap.put("beerName", "new Name");

        mockMvc.perform(patch(BeerController.BEER_PATH_ID, beerToPatch.getId())
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beerMap))) // body json of the patch request (key-value)
            .andExpect(status().isNoContent());

        verify(beerService).patchBeerById(uuidArgumentCaptor.capture(), beerArgumentCaptor.capture());
        assertThat(beerToPatch.getId()).isEqualTo(uuidArgumentCaptor.getValue()); 
        assertThat(beerMap.get("beerName")).isEqualTo(beerArgumentCaptor.getValue().getBeerName()); // parameter passed in call to service check  
    }

}
