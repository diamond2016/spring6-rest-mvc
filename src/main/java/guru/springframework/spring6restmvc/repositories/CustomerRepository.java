package guru.springframework.spring6restmvc.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import guru.springframework.spring6restmvc.entities.Beer;

public interface CustomerRepository extends JpaRepository<Beer, UUID> {

}
