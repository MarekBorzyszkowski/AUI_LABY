package com.example.petsbreeds.pets.breed.dto;

import com.example.petsbreeds.pets.breed.entity.Breed;
import com.example.petsbreeds.pets.dogs.entity.Dog;
import lombok.*;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class GetBreedResponse {
    private Long id;
    private String name;
    private boolean pure;
    private Set<String> dogSet;

    public static Function<Breed, GetBreedResponse> entityToDtoMapper(){
        return breed -> GetBreedResponse.builder()
                .id(breed.getId())
                .name(breed.getName())
                .pure(breed.isPure())
                .dogSet(breed.getDogSet()
                        .stream()
                        .map(Dog::getName)
                        .collect(Collectors.toSet()))
                .build();
    }
}
