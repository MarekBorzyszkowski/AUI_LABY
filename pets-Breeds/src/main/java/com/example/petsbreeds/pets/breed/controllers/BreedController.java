package com.example.petsbreeds.pets.breed.controllers;

import com.example.petsbreeds.pets.breed.dto.CreateBreedRequest;
import com.example.petsbreeds.pets.breed.dto.GetBreedResponse;
import com.example.petsbreeds.pets.breed.dto.GetBreedsResponse;
import com.example.petsbreeds.pets.breed.dto.UpdateBreedRequest;
import com.example.petsbreeds.pets.breed.entity.Breed;
import com.example.petsbreeds.pets.breed.service.BreedService;
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
import java.util.Optional;

@RestController
@RequestMapping("api/breeds")
public class BreedController {

    private BreedService breedService;

    @Autowired
    public BreedController(BreedService breedService){
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
            RestTemplate restTemplate = new RestTemplate();
            HttpClient httpClient = HttpClientBuilder.create().build();
            restTemplate.setRequestFactory(new
                    HttpComponentsClientHttpRequestFactory(httpClient));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            JSONObject personJsonObject = new JSONObject();
            personJsonObject.put("name",  request.getName());
            HttpEntity<String> newRequest = new HttpEntity<>(personJsonObject.toString(), headers);
            restTemplate.exchange(String.format("http://localhost:8082/api/breeds/%d", id), HttpMethod.PATCH, newRequest, String.class);

            return ResponseEntity.accepted().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}
