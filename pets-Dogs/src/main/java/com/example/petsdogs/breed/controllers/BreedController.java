package com.example.petsdogs.breed.controllers;

import com.example.petsdogs.breed.dto.CreateBreedRequest;
import com.example.petsdogs.breed.dto.UpdateBreedRequest;
import com.example.petsdogs.breed.entity.Breed;
import com.example.petsdogs.breed.service.BreedService;
import com.example.petsdogs.dogs.services.DogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@RestController
@RequestMapping("api/breeds")
public class BreedController {

    private DogService dogService;
    private BreedService breedService;

    @Autowired
    public BreedController(DogService dogService, BreedService breedService){
        this.dogService = dogService;
        this.breedService = breedService;
    }

    @PostMapping
    public ResponseEntity<Void> createBreed(@RequestBody CreateBreedRequest request, UriComponentsBuilder builder){
        Breed breed = breedService.create(CreateBreedRequest.dtoToEntityMapper()
                .apply(request));
        return ResponseEntity.created(builder.pathSegment("api", "breeds", "{id}")
                .buildAndExpand(breed.getId()).toUri()).build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteBreed(@PathVariable("id") long id){
        Optional<Breed> breed = breedService.find(id);
        if(breed.isPresent()){
            breedService.delete(breed.get().getId());
            return ResponseEntity.accepted().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("{id}")
    public ResponseEntity<Void> updateBreed(@RequestBody UpdateBreedRequest request, @PathVariable("id") long id){
        Optional<Breed> breed = breedService.find(id);
        if(breed.isPresent()){
            UpdateBreedRequest.dtoToEntityMapper().apply(breed.get(), request);
            breedService.update(breed.get());
            return ResponseEntity.accepted().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}
