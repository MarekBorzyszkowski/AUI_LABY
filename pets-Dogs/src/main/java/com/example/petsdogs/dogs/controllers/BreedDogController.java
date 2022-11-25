package com.example.petsdogs.dogs.controllers;

import com.example.petsdogs.breed.entity.Breed;
import com.example.petsdogs.breed.service.BreedService;
import com.example.petsdogs.dogs.dto.*;
import com.example.petsdogs.dogs.entity.Dog;
import com.example.petsdogs.dogs.services.DogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@RestController
@RequestMapping("api/breeds/{id}/dogs")
public class BreedDogController {
    private DogService dogService;
    private BreedService breedService;

    @Autowired
    public BreedDogController(DogService dogService, BreedService breedService){
        this.dogService = dogService;
        this.breedService = breedService;
    }

    @GetMapping
    public ResponseEntity<GetDogsResponse> getDogs(@PathVariable Long id){
        return ResponseEntity.ok(GetDogsResponse.entityToDtoMapper()
                .apply(dogService.findAllByBreed(breedService.find(id).orElse(null))));
    }

    @GetMapping("{id_dog}")
    public ResponseEntity<GetDogResponse> getDog(@PathVariable("id") Long id, @PathVariable Long id_dog){
        return dogService.findAllByBreed(breedService.find(id)
                        .orElse(null))
                .stream()
                .filter(dog -> dog.getId()
                        .equals(id_dog))
                .findFirst()
                .map(dog -> ResponseEntity.ok(GetDogResponse.entityToDtoMapper().apply(dog)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<Void> createDog(@RequestBody CreateBreedDogRequest request, UriComponentsBuilder builder, @PathVariable Long id) {
        Dog dog = dogService.create(CreateBreedDogRequest.dtoToEntityMapper(breedService.find(id).orElseThrow())
                .apply(request));
        return ResponseEntity.created(builder.pathSegment("api", "dogs", "{id}")
                .buildAndExpand(dog.getId()).toUri()).build();
    }

    @DeleteMapping("{id_dog}")
    public ResponseEntity<Void> deleteDog(@PathVariable Long id, @PathVariable("id_dog") Long id_dog) {
        Optional<Dog> dog = dogService.findAllByBreed(breedService.find(id)
                        .orElse(null))
                .stream()
                .filter(d -> d.getId()
                        .equals(id_dog))
                .findFirst();
        if(dog.isPresent()){
            dogService.delete(dog.get().getId());
            return ResponseEntity.accepted().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("{id_dog}")
    public ResponseEntity<Void> updateDog(@RequestBody UpdateDogRequest request,@PathVariable Long id, @PathVariable("id_dog") Long id_dog ) {
        Optional<Dog> dog = dogService.findAllByBreed(breedService.find(id)
                        .orElse(null))
                .stream()
                .filter(d -> d.getId()
                        .equals(id_dog))
                .findFirst();
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
