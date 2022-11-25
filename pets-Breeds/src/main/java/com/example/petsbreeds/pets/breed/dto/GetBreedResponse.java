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
public class GetBreedResponse {
    private Long id;
    private String name;
    private boolean pure;

    public static Function<Breed, GetBreedResponse> entityToDtoMapper(){
        return breed -> GetBreedResponse.builder()
                .id(breed.getId())
                .name(breed.getName())
                .pure(breed.isPure())
                .build();
    }
}
