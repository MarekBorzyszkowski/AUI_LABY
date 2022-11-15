package com.example.petsdogs.breed.controllers;

import com.example.petsdogs.breed.dto.CreateBreedRequest;
import com.example.petsdogs.breed.dto.UpdateBreedRequest;
import com.example.petsdogs.breed.entity.Breed;
import com.example.petsdogs.breed.service.BreedService;
import com.example.petsdogs.dogs.dto.GetDogResponse;
import com.example.petsdogs.dogs.dto.GetDogsResponse;
import com.example.petsdogs.dogs.entity.Dog;
import com.example.petsdogs.dogs.services.DogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Transactional
    @GetMapping("{id}/dogs")
    public ResponseEntity<GetDogsResponse> getBreedDogs(@PathVariable("id") long id){
        return breedService.find(id)
                .map(breed -> ResponseEntity.ok(GetDogsResponse.entityToDtoMapper().apply(breed.getDogSet())))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Transactional
    @GetMapping("{id}/dogs/{dId}")
    public ResponseEntity<GetDogResponse> getBreedDogs(@PathVariable("id") long id, @PathVariable("dId") long dId){
        Set<Dog> dogs = breedService.find(id)
                .map(Breed::getDogSet)
                .orElseGet(Collections::emptySet);
        if(!dogs.isEmpty()){
            List<Dog> dogsList= dogs.stream().filter(d -> d.getId()==dId).collect(Collectors.toList());
            if(!dogsList.isEmpty()){
                return ResponseEntity.ok(GetDogResponse.entityToDtoMapper().apply(dogsList.get(0)));
            }
            else{
                return ResponseEntity.notFound().build();
            }
        }
        else{
            return ResponseEntity.notFound().build();
        }
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
