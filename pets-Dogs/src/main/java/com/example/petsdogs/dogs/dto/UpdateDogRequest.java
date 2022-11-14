package com.example.petsdogs.dogs.dto;

import com.example.petsdogs.dogs.entity.Dog;
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
    private Integer age;
    private Double height;

    public static BiFunction<Dog, UpdateDogRequest, Dog> dtoToEntityMapper(){
        return (dog, request) -> {
            if (!request.getName().isEmpty()){
                dog.setName(request.getName());
            }
            if(canAgeBeChanged(request)) {
                dog.setAge(request.getAge());
            }
            if(canHeightBeChanged(request)) {
                dog.setHeight(request.getHeight());
            }
            return dog;
        };
    }

    private static boolean canAgeBeChanged(UpdateDogRequest request) {
        if(!isNull(request.getAge())){
            return request.getAge() >= 0;
        }
        else{
            return false;
        }
    }

    private static boolean canHeightBeChanged(UpdateDogRequest request) {
        if(!isNull(request.getHeight())){
            return request.getHeight() > 0.0;
        }
        else{
            return false;
        }
    }
}
