package guru.springframework.spring6restmvc.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.services.BeerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController

public class BeerController {
    public static final String BEER_PATH = "/api/v1/beer";
    public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";
    private final BeerService beerService;

    @GetMapping(BEER_PATH_ID)
    // beerId is also a path variable in requests
    public BeerDTO getBeerById(@PathVariable("beerId") UUID beerId) {

        log.debug("get Beer from ID - in controller id: {}", beerId);
        
        return beerService.getBeerById(beerId).orElseThrow(NotFoundException::new);
    }

    @GetMapping(BEER_PATH)
    public List<BeerDTO> listBeers() {

        log.debug("List Beers - in controller ");
        
        return beerService.listBeers();
    }

    // if in this controller will occur NotFound SpringBoot, will return the custom exception instead as response
    //@ExceptionHandler(NotFoundException.class)
    //public ResponseEntity<Void> handleNotFoundException() {
    //    System.out.println("In exception handler...");
    //    return ResponseEntity.notFound().build();
    //}

    @PostMapping(BEER_PATH) 
    public ResponseEntity<Void> handlePost(@Validated @RequestBody BeerDTO beer) { 
        //@Validated in RequestBody activate JPA validation

        BeerDTO savedBeer = beerService.saveNewBeer(beer); 

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", BEER_PATH + "/" + savedBeer.getId().toString());

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping(BEER_PATH_ID)
    public ResponseEntity<Void> updateBeerById(@PathVariable("beerId") UUID beerId, @Validated @RequestBody BeerDTO beer) { 
    //@Validated in RequestBody activate JPA validation
        
        log.debug("update Beer of ID - in controller id: {}", beerId);
        beerService.updateBeerById(beerId, beer).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(BEER_PATH_ID)
    public ResponseEntity<Void> deleteBeerById(@PathVariable("beerId") UUID beerId) {

        log.debug("delete Beer of ID - in controller id: {}", beerId);
        if (!beerService.deleteBeerById(beerId))
            throw new NotFoundException();
         
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        
    }

    @PatchMapping(BEER_PATH_ID)
    public ResponseEntity<Void> patchBeerById(@PathVariable("beerId") UUID beerId, @Validated @RequestBody BeerDTO beer) {
        //@Validated in RequestBody activate JPA validation
        
        beerService.patchBeerById(beerId, beer).orElseThrow(NotFoundException::new);
        log.debug("patch Beer patch of ID - in controller id: {}", beerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
