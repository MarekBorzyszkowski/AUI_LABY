package com.example.petsdogs.dogs.dto;


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
public class GetDogResponse {
    private Long id;
    private String name;
    private int age;
    private double height;
    private String breed;

    public static Function<Dog, GetDogResponse> entityToDtoMapper(){
        return dog -> GetDogResponse.builder()
                .id(dog.getId())
                .name(dog.getName())
                .age(dog.getAge())
                .height(dog.getHeight())
                .breed(dog.getBreed().getName())
                .build();
    }
}
