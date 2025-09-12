package guru.springframework.spring6restmvc.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import guru.springframework.spring6restmvc.model.Beer;
import guru.springframework.spring6restmvc.services.BeerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RestController
public class BeerController {
    private final BeerService beerService;

    public Beer getBeerByID(UUID id) {

        log.debug("Get Beer by Id - in controller. Id: " + id.toString());
        
        return beerService.getBeerById(id);
    }

    @RequestMapping("/api/v1/beer")
    public List<Beer> listBeers() {

        log.debug("List Beers - in controller ");
        
        return beerService.listBeers();
    }
}
