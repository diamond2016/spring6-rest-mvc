package guru.springframework.spring6restmvc.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import guru.springframework.spring6restmvc.model.Beer;
import guru.springframework.spring6restmvc.services.BeerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/beer")
public class BeerController {
    private final BeerService beerService;

    public Beer getBeerByID(UUID id) {

        log.debug("Get Beer by Id - in controller. Id: " + id.toString());
        
        return beerService.getBeerById(id);
    }

    @RequestMapping(method = RequestMethod.GET, value = "{beerId}") 
    // beerId is also a path variable in requests
    public Beer getBeerById(@PathVariable("beerId") UUID beerId) {

        log.debug("get Beer from ID - in controller id: " + beerId);
        
        return beerService.getBeerById(beerId);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Beer> listBeers() {

        log.debug("List Beers - in controller ");
        
        return beerService.listBeers();
    }
}
