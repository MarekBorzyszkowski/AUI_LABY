package com.example.petsdogs.breed.dto;

import com.example.petsdogs.breed.entity.Breed;
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
public class UpdateBreedRequest {

    private String name;
    public static BiFunction<Breed, UpdateBreedRequest, Breed> dtoToEntityMapper(){
        return (breed, request) -> {
            if(!request.getName().isEmpty()) {
                breed.setName(request.getName());
            }
            return breed;
        };
    }
}
