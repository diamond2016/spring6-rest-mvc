package guru.springframework.spring6restmvc.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import guru.springframework.spring6restmvc.model.Beer;
import guru.springframework.spring6restmvc.services.BeerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/beer") //no final shash
public class BeerController {
    private final BeerService beerService;

    @GetMapping("{beerId}")
    // beerId is also a path variable in requests
    public Beer getBeerById(@PathVariable("beerId") UUID beerId) {

        log.debug("get Beer from ID - in controller id: {}", beerId);
        
        return beerService.getBeerById(beerId);
    }

    @GetMapping
    public List<Beer> listBeers() {

        log.debug("List Beers - in controller ");
        
        return beerService.listBeers();
    }

    @PostMapping
    public ResponseEntity<Void> handlePost(@RequestBody Beer beer) {

        Beer savedBeer = beerService.saveNewBeer(beer); 

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/beer/" + savedBeer.getId().toString());

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
}
