package com.example.petsdogs.dogs.controllers;

import com.example.petsdogs.breed.service.BreedService;
import com.example.petsdogs.dogs.dto.CreateDogRequest;
import com.example.petsdogs.dogs.dto.GetDogResponse;
import com.example.petsdogs.dogs.dto.GetDogsResponse;
import com.example.petsdogs.dogs.dto.UpdateDogRequest;
import com.example.petsdogs.dogs.entity.Dog;
import com.example.petsdogs.dogs.services.DogService;
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
    public ResponseEntity<Void> createDog(@RequestBody CreateDogRequest request, UriComponentsBuilder builder) throws JSONException {
        Dog dog = dogService.create(CreateDogRequest.dtoToEntityMapper(breed -> breedService.find(breed).orElseThrow())
                .apply(request));
        var restTemplate = new RestTemplate();
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var personJsonObject = new JSONObject();
        personJsonObject.put("name",  dog.getName());
        personJsonObject.put("breed", request.getBreed());
        HttpEntity<String> newRequest = new HttpEntity<>(personJsonObject.toString(), headers);
        restTemplate.postForLocation("http://localhost:8081/api/dogs", newRequest);
        return ResponseEntity.created(builder.pathSegment("api", "dogs", "{id}")
                .buildAndExpand(dog.getId()).toUri()).build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteDog(@PathVariable("id") long id) {
        Optional<Dog> dog = dogService.find(id);
        if(dog.isPresent()){
            dogService.delete(dog.get().getId());
            var restTemplate = new RestTemplate();
            restTemplate.delete(String.format("http://localhost:8081/api/dogs/%d", id));
            return ResponseEntity.accepted().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("{id}")
    public ResponseEntity<Void> updateDog(@RequestBody UpdateDogRequest request, @PathVariable("id") long id) throws JSONException {
        Optional<Dog> dog = dogService.find(id);
        if(dog.isPresent()) {
            UpdateDogRequest.dtoToEntityMapper().apply(dog.get(), request);
            dogService.update(dog.get());
            var restTemplate = new RestTemplate();
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            var personJsonObject = new JSONObject();
            personJsonObject.put("name",  request.getName());
            HttpEntity<String> newRequest = new HttpEntity<>(personJsonObject.toString(), headers);
            restTemplate.patchForObject(String.format("http://localhost:8081/api/dogs/%d", id), newRequest, String.class);
            return ResponseEntity.accepted().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}
