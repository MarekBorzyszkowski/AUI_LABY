package com.example.petsbreeds.initialize;

import com.example.petsbreeds.pets.breed.entity.Breed;
import com.example.petsbreeds.pets.dogs.entity.Dog;
import com.example.petsbreeds.pets.breed.service.BreedService;
import com.example.petsbreeds.pets.dogs.services.DogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class InitializeData {

    private DogService dogService;
    private BreedService breedService;

    @Autowired
    public InitializeData(DogService dogService, BreedService breedService){
        this.dogService = dogService;
        this.breedService = breedService;
    }

    @PostConstruct
    private synchronized void init(){

        Breed corgi = Breed.builder().name("Corgi").pure(true).build();
        Breed germanSheppard = Breed.builder().name("German Sheppard").pure(true).build();

        breedService.create(corgi);
        breedService.create(germanSheppard);

        Dog pinki = Dog.builder().name("Pinki").breed(corgi).build();
        Dog latek = Dog.builder().name("Latek").breed(germanSheppard).build();
        Dog pysiek = Dog.builder().name("Pysiek").breed(corgi).build();
        Dog aqq = Dog.builder().name("Pysiek").breed(corgi).build();

        dogService.create(pinki);
        dogService.create(latek);
        dogService.create(pysiek);
        dogService.create(aqq);
    }


}
