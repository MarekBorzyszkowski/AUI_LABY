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
public class CreateDogRequest {
    private String name;
    private int age;
    private double height;
    private String breed;

    public static Function<CreateDogRequest, Dog> dtoToEntityMapper(Function<String, Breed> breedFunction){
        return request -> Dog.builder()
                .name(request.getName())
                .age(request.getAge())
                .height(request.getHeight())
                .breed(breedFunction.apply(request.getBreed()))
                .build();
    }
}
