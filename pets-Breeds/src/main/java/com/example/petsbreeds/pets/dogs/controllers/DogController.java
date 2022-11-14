package com.example.petsbreeds.pets.dogs.controllers;

import com.example.petsbreeds.pets.breed.service.BreedService;
import com.example.petsbreeds.pets.dogs.dto.CreateDogRequest;
import com.example.petsbreeds.pets.dogs.dto.UpdateDogRequest;
import com.example.petsbreeds.pets.dogs.entity.Dog;
import com.example.petsbreeds.pets.dogs.services.DogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@RestController
@RequestMapping("api/dogs")
public class DogController {
    private DogService dogService;
    private BreedService breedService;

    @Autowired
    public DogController(DogService dogService, BreedService breedService){
        this.dogService = dogService;
        this.breedService = breedService;
    }

    @PostMapping
    public ResponseEntity<Void> createDog(@RequestBody CreateDogRequest request, UriComponentsBuilder builder){
        Dog dog = dogService.create(CreateDogRequest.dtoToEntityMapper(breed -> breedService.find(breed).orElseThrow())
                .apply(request));
        return ResponseEntity.created(builder.pathSegment("api", "dogs", "{id}")
                .buildAndExpand(dog.getId()).toUri()).build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteDog(@PathVariable("id") long id){
        Optional<Dog> dog = dogService.find(id);
        if(dog.isPresent()){
            dogService.delete(dog.get().getId());
            return ResponseEntity.accepted().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("{id}")
    public ResponseEntity<Void> updateDog(@RequestBody UpdateDogRequest request, @PathVariable("id") long id){
        Optional<Dog> dog = dogService.find(id);
        if(dog.isPresent()){
            UpdateDogRequest.dtoToEntityMapper().apply(dog.get(), request);
            dogService.update(dog.get());
            return ResponseEntity.accepted().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}
