package com.example.petsbreeds.pets.dogs.dto;

import com.example.petsbreeds.pets.breed.entity.Breed;
import com.example.petsbreeds.pets.dogs.entity.Dog;
import lombok.*;

import java.util.function.Function;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class CreateDogRequest {
    private String name;
    private String breed;

    public static Function<CreateDogRequest, Dog> dtoToEntityMapper(Function<String, Breed> breedFunction){
        return request -> Dog.builder()
                .name(request.getName())
                .breed(breedFunction.apply(request.getBreed()))
                .build();
    }
}
