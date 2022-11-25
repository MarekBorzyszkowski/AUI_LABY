package com.example.petsdogs.dogs.controllers;

import com.example.petsdogs.breed.service.BreedService;
import com.example.petsdogs.dogs.dto.CreateDogRequest;
import com.example.petsdogs.dogs.dto.GetDogResponse;
import com.example.petsdogs.dogs.dto.GetDogsResponse;
import com.example.petsdogs.dogs.dto.UpdateDogRequest;
import com.example.petsdogs.dogs.entity.Dog;
import com.example.petsdogs.dogs.services.DogService;
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

    @GetMapping
    public ResponseEntity<GetDogsResponse> getDogs(){
        return ResponseEntity.ok(GetDogsResponse.entityToDtoMapper()
                .apply(dogService.findAll()));
    }

    @GetMapping("{id}")
    public ResponseEntity<GetDogResponse> getDog(@PathVariable("id") long id){
        return dogService.find(id)
                .map(dog -> ResponseEntity.ok(GetDogResponse.entityToDtoMapper().apply(dog)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Void> createDog(@RequestBody CreateDogRequest request, UriComponentsBuilder builder) {
        Dog dog = dogService.create(CreateDogRequest.dtoToEntityMapper(breed -> breedService.find(breed).orElseThrow())
                .apply(request));
        return ResponseEntity.created(builder.pathSegment("api", "dogs", "{id}")
                .buildAndExpand(dog.getId()).toUri()).build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteDog(@PathVariable("id") long id) {
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
    public ResponseEntity<Void> updateDog(@RequestBody UpdateDogRequest request, @PathVariable("id") long id) {
        Optional<Dog> dog = dogService.find(id);
        if(dog.isPresent()) {
            UpdateDogRequest.dtoToEntityMapper().apply(dog.get(), request);
            dogService.update(dog.get());
            return ResponseEntity.accepted().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}
