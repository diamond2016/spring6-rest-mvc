package guru.springframework.spring6restmvc.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
		// TODO: implement method
		return null;
	}

	@Override
	public Optional<BeerDTO> getBeerById(UUID id) {
		// TODO: implement method
		return Optional.empty();
	}

	@Override
	public BeerDTO saveNewBeer(BeerDTO beerDTO) {
		// TODO: implement method
		return null;
	}

	@Override
	public void updateBeerById(UUID id, BeerDTO beerDTO) {
		// TODO: implement method
	}

	@Override
	public void deleteBeerById(UUID id) {
		// TODO: implement method
	}

	@Override
	public void patchBeerById(UUID id, BeerDTO beerDTO) {
		// TODO: implement method
	}
}
