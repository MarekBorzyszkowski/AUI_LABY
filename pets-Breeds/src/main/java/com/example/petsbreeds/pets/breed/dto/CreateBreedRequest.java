package com.example.petsbreeds.pets.breed.dto;

import com.example.petsbreeds.pets.breed.entity.Breed;
import lombok.*;

import java.util.function.Function;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class CreateBreedRequest {
    private String name;
    private boolean pure;

    public static Function<CreateBreedRequest, Breed> dtoToEntityMapper(){
        return request -> Breed.builder()
                .name(request.getName())
                .pure(request.isPure())
                .build();
    }
}
