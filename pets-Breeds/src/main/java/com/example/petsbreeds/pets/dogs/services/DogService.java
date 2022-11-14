package com.example.petsbreeds.pets.dogs.services;

import com.example.petsbreeds.pets.dogs.entity.Dog;
import com.example.petsbreeds.pets.dogs.repositories.DogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class DogService {

    private DogRepository repository;

    @Autowired
    public DogService(DogRepository repository){
        this.repository = repository;
    }

    public Optional<Dog> find(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Dog create(Dog dog){
        return this.repository.save(dog);
    }

    @Transactional
    public Dog update(Dog dog){
        return this.repository.save(dog);
    }

    @Transactional
    public void delete(Long id){
        this.repository.deleteById(id);
    }
}
