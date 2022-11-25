package com.example.petsdogs.dogs.repositories;

import com.example.petsdogs.breed.entity.Breed;
import com.example.petsdogs.dogs.entity.Dog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DogRepository extends JpaRepository<Dog, Long> {
    List<Dog> findAllByBreed(Breed breed);
}
