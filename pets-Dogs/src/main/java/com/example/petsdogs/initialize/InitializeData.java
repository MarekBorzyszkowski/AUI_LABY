package com.example.petsdogs.initialize;

import com.example.petsdogs.breed.entity.Breed;
import com.example.petsdogs.dogs.entity.Dog;
import com.example.petsdogs.breed.service.BreedService;
import com.example.petsdogs.dogs.services.DogService;
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

        Breed corgi = Breed.builder().name("Corgi").build();
        Breed germanSheppard = Breed.builder().name("German Sheppard").build();

        breedService.create(corgi);
        breedService.create(germanSheppard);

        Dog pinki = Dog.builder().name("Pinki").breed(corgi).age(5).height(1.2).build();
        Dog latek = Dog.builder().name("Latek").breed(germanSheppard).age(10).height(1.5).build();
        Dog pysiek = Dog.builder().name("Pysiek").breed(corgi).age(1).height(0.9).build();
        Dog aqq = Dog.builder().name("Pysiek").breed(corgi).age(1).height(0.9).build();

        dogService.create(pinki);
        dogService.create(latek);
        dogService.create(pysiek);
        dogService.create(aqq);
    }


}
