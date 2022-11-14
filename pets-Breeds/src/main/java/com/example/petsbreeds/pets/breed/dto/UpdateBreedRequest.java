package com.example.petsbreeds.pets.breed.dto;

import com.example.petsbreeds.pets.breed.entity.Breed;
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
    private Boolean pure;

    public static BiFunction<Breed, UpdateBreedRequest, Breed> dtoToEntityMapper(){
        return (breed, request) -> {
            if(!request.getName().isEmpty()) {
                breed.setName(request.getName());
            }
            if(canPurityBeChanged(request)) {
                breed.setPure(request.getPure());
            }
            return breed;
        };
    }

    private static boolean canPurityBeChanged(UpdateBreedRequest request) {
        if(!isNull(request.getPure())){
            return request.getPure();
        }
        else{
            return false;
        }
    }
}
