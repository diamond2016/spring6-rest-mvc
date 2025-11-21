package guru.springframework.spring6restmvc.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import guru.springframework.spring6restmvc.mappers.BeerMapper;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;

@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {
    // being non static will injected by Spring
    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public List<BeerDTO> listBeers() {
        return beerRepository.findAll()
                .stream()
                .map(beerMapper::beerToBeerDTO)
                .collect(Collectors.toList());
    }
/*
 *  beerRepository.findById(id).map(beerMapper::beerToBeerDTO) clearly expresses the intent: 
 * "Find the beer by ID, and if it exists, map it to a BeerDTO."
    Null-Safety: The map operation is only performed if the Optional returned by findById contains a value. 
    If it's empty, map does nothing and returns an empty Optional.
 */

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        return beerRepository.findById(id).map(beerMapper::beerToBeerDTO);
    }

    @Override
    public BeerDTO saveNewBeer(BeerDTO beerDTO) {
        return beerMapper.beerToBeerDTO(beerRepository.save(beerMapper.beerDTOToBeer(beerDTO)));    
    }

    @Override
    public Optional<BeerDTO> updateBeerById(UUID id, BeerDTO beerDTO) {
        return beerRepository.findById(id).map(existingBeer -> {    // map returns authomatically Optional.empty if not present 
            existingBeer.setBeerName(beerDTO.getBeerName());
            existingBeer.setBeerStyle(beerDTO.getBeerStyle());
            existingBeer.setUpc(beerDTO.getUpc());
            existingBeer.setPrice(beerDTO.getPrice());
            existingBeer.setQuantityOnHand(beerDTO.getQuantityOnHand());
            existingBeer.setLastModifiedDate(LocalDateTime.now());
            return beerMapper.beerToBeerDTO(beerRepository.save(existingBeer));
        });
    }

    @Override
    public Boolean deleteBeerById(UUID id) {
        if (beerRepository.existsById(id)) {
            beerRepository.deleteById(id);
            return true;
        }
        
        return false;
    }

    @Override
    public Optional<BeerDTO> patchBeerById(UUID id, BeerDTO beerDTO) {
        return beerRepository.findById(id).map(existingBeer -> {    // map returns authomatically Optional.empty if not present 
            existingBeer.setBeerName(beerDTO.getBeerName());
            existingBeer.setBeerStyle(beerDTO.getBeerStyle());
            existingBeer.setUpc(beerDTO.getUpc());
            existingBeer.setPrice(beerDTO.getPrice());
            existingBeer.setQuantityOnHand(beerDTO.getQuantityOnHand());
            existingBeer.setLastModifiedDate(LocalDateTime.now());
            return beerMapper.beerToBeerDTO(beerRepository.save(existingBeer));
        });
    }
}
