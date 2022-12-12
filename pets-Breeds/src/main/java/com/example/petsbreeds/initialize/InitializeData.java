package com.example.petsbreeds.initialize;

import com.example.petsbreeds.pets.breed.entity.Breed;
import com.example.petsbreeds.pets.breed.service.BreedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class InitializeData {

    private BreedService breedService;

    @Autowired
    public InitializeData(BreedService breedService){
        this.breedService = breedService;
    }

    @PostConstruct
    private synchronized void init(){
        Breed corgi = Breed.builder().name("Corgi").pure(true).build();
        Breed germanSheppard = Breed.builder().name("German Sheppard").pure(true).build();

        breedService.create(corgi);
        breedService.create(germanSheppard);
    }


}
