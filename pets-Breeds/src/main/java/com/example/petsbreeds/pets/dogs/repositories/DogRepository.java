package com.example.petsbreeds.pets.dogs.repositories;

import com.example.petsbreeds.pets.dogs.entity.Dog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DogRepository extends JpaRepository<Dog, Long> {
}
