package com.example.petsbreeds.pets.breed.service;

import com.example.petsbreeds.pets.breed.entity.Breed;
import com.example.petsbreeds.pets.breed.repository.BreedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class BreedService {

    private BreedRepository repository;

    @Autowired
    public BreedService(BreedRepository repository){
        this.repository = repository;
    }

    public Optional<Breed> find(String name) {
        return repository.findByName(name);
    }

    public Optional<Breed> find(Long id) {
        return repository.findById(id);
    }

    public List<Breed> findAll(){
        return this.repository.findAll();
    }

    @Transactional
    public Breed create(Breed breed){
        return this.repository.save(breed);
    }

    @Transactional
    public Breed update(Breed breed) {
        return this.repository.save(breed);
    }

    @Transactional
    public void delete(Long id){this.repository.deleteById(id);}
}
