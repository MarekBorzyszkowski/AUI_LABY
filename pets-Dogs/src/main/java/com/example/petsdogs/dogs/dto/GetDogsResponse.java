package com.example.petsdogs.dogs.dto;

import com.example.petsdogs.dogs.entity.Dog;
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
public class GetDogsResponse {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @ToString
    @EqualsAndHashCode
    public static class DogSimple{
        private Long id;
        private String name;
    }

    @Singular
    private List<DogSimple> dogs;

    public static Function<Collection<Dog>, GetDogsResponse> entityToDtoMapper(){
        return dogs -> {
            GetDogsResponseBuilder response = GetDogsResponse.builder();
            dogs.stream().map(dog -> DogSimple.builder()
                    .id(dog.getId())
                    .name(dog.getName())
                    .build()).forEach(response::dog);
            return response.build();
        };
    }

}
