package com.example.petsdogs.dogs.dto;

import com.example.petsdogs.breed.entity.Breed;
import com.example.petsdogs.dogs.entity.Dog;
import lombok.*;

import java.util.function.Function;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class CreateBreedDogRequest {
    private String name;
    private int age;
    private double height;

    public static Function<CreateBreedDogRequest, Dog> dtoToEntityMapper(Breed breedName){
        return request -> Dog.builder()
                .name(request.getName())
                .age(request.getAge())
                .height(request.getHeight())
                .breed(breedName)
                .build();
    }
}
