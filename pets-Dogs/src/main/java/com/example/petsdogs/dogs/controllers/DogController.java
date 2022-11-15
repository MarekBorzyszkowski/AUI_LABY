package com.example.petsdogs.dogs.controllers;

import com.example.petsdogs.breed.service.BreedService;
import com.example.petsdogs.dogs.dto.CreateDogRequest;
import com.example.petsdogs.dogs.dto.GetDogResponse;
import com.example.petsdogs.dogs.dto.GetDogsResponse;
import com.example.petsdogs.dogs.dto.UpdateDogRequest;
import com.example.petsdogs.dogs.entity.Dog;
import com.example.petsdogs.dogs.services.DogService;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject personJsonObject = new JSONObject();
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
            RestTemplate restTemplate = new RestTemplate();
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
            RestTemplate restTemplate = new RestTemplate();
            HttpClient httpClient = HttpClientBuilder.create().build();
            restTemplate.setRequestFactory(new
                    HttpComponentsClientHttpRequestFactory(httpClient));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            JSONObject personJsonObject = new JSONObject();
            personJsonObject.put("name",  request.getName());
            HttpEntity<String> newRequest = new HttpEntity<>(personJsonObject.toString(), headers);
            restTemplate.exchange(String.format("http://localhost:8081/api/dogs/%d", id), HttpMethod.PATCH, newRequest, String.class);
            return ResponseEntity.accepted().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}
