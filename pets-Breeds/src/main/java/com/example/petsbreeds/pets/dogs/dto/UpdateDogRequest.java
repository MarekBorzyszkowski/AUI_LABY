package com.example.petsbreeds.pets.dogs.dto;

import com.example.petsbreeds.pets.dogs.entity.Dog;
import lombok.*;

import java.util.function.BiFunction;

import static java.util.Objects.isNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class UpdateDogRequest {
    private String name;

    public static BiFunction<Dog, UpdateDogRequest, Dog> dtoToEntityMapper(){
        return (dog, request) -> {
            if (!request.getName().isEmpty()){
                dog.setName(request.getName());
            }
            return dog;
        };
    }
}
