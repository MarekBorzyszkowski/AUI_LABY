package com.example.petsdogs.breed.service;

import com.example.petsdogs.breed.entity.Breed;
import com.example.petsdogs.breed.repository.BreedRepository;
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
