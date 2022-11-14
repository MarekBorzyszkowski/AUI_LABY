package com.example.petsbreeds.pets.breed.dto;

import com.example.petsbreeds.pets.breed.entity.Breed;
import lombok.*;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class GetBreedsResponse {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @ToString
    @EqualsAndHashCode
    public static class BreedSimple{
        private Long id;
        private String name;
    }

    @Singular
    private List<BreedSimple> breeds;

    public static Function<Collection<Breed>, GetBreedsResponse> entityToDtoMapper(){
        return breeds -> {
            GetBreedsResponse.GetBreedsResponseBuilder response = GetBreedsResponse.builder();
            breeds.stream().map(breed -> GetBreedsResponse.BreedSimple.builder()
                    .id(breed.getId())
                    .name(breed.getName())
                    .build()).forEach(response::breed);
            return response.build();
        };
    }
}
