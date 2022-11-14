package com.example.petsbreeds.pets.breed.controllers;

import com.example.petsbreeds.pets.breed.dto.CreateBreedRequest;
import com.example.petsbreeds.pets.breed.dto.GetBreedResponse;
import com.example.petsbreeds.pets.breed.dto.GetBreedsResponse;
import com.example.petsbreeds.pets.breed.dto.UpdateBreedRequest;
import com.example.petsbreeds.pets.breed.entity.Breed;
import com.example.petsbreeds.pets.breed.service.BreedService;
import com.example.petsbreeds.pets.dogs.services.DogService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
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

    @GetMapping
    public ResponseEntity<GetBreedsResponse> getBreeds(){
        return ResponseEntity.ok(GetBreedsResponse.entityToDtoMapper()
                .apply(breedService.findAll()));
    }

    @Transactional
    @GetMapping("{id}")
    public ResponseEntity<GetBreedResponse> getBreed(@PathVariable("id") long id){
        return breedService.find(id)
                .map(breed -> ResponseEntity.ok(GetBreedResponse.entityToDtoMapper().apply(breed)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Void> createBreed(@RequestBody CreateBreedRequest request, UriComponentsBuilder builder) throws JSONException {
        Breed breed = breedService.create(CreateBreedRequest.dtoToEntityMapper()
                .apply(request));
        var restTemplate = new RestTemplate();
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var personJsonObject = new JSONObject();
        personJsonObject.put("name",  request.getName());
        HttpEntity<String> newRequest = new HttpEntity<>(personJsonObject.toString(), headers);
        restTemplate.postForLocation("http://localhost:8082/api/breeds", newRequest);
        return ResponseEntity.created(builder.pathSegment("api", "breeds", "{id}")
                .buildAndExpand(breed.getId()).toUri()).build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteBreed(@PathVariable("id") long id){
        Optional<Breed> breed = breedService.find(id);
        if(breed.isPresent()){
            breedService.delete(breed.get().getId());
            new RestTemplate().delete(String.format("http://localhost:8082/api/breeds/%d", id));
            return ResponseEntity.accepted().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("{id}")
    public ResponseEntity<Void> updateBreed(@RequestBody UpdateBreedRequest request, @PathVariable("id") long id) throws JSONException {
        Optional<Breed> breed = breedService.find(id);
        if(breed.isPresent()){
            UpdateBreedRequest.dtoToEntityMapper().apply(breed.get(), request);
            breedService.update(breed.get());

            var restTemplate = new RestTemplate();
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            var personJsonObject = new JSONObject();
            personJsonObject.put("name",  request.getName());
            HttpEntity<String> newRequest = new HttpEntity<>(personJsonObject.toString(), headers);
            restTemplate.patchForObject(String.format("http://localhost:8082/api/breeds/%d", id), newRequest, String.class);
            return ResponseEntity.accepted().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}
