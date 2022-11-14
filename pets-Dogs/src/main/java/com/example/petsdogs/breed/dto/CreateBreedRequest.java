package com.example.petsdogs.breed.dto;

import com.example.petsdogs.breed.entity.Breed;
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

    public static Function<CreateBreedRequest, Breed> dtoToEntityMapper(){
        return request -> Breed.builder()
                .name(request.getName())
                .build();
    }
}
