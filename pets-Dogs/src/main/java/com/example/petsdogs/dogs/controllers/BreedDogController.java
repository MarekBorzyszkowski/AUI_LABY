package com.example.petsdogs.dogs.controllers;

import com.example.petsdogs.breed.service.BreedService;
import com.example.petsdogs.dogs.dto.GetDogResponse;
import com.example.petsdogs.dogs.dto.GetDogsResponse;
import com.example.petsdogs.dogs.services.DogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
