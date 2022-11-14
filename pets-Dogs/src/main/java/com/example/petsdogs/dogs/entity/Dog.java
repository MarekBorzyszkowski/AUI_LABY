package com.example.petsdogs.dogs.entity;


import com.example.petsdogs.breed.entity.Breed;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "dogs")
public class Dog implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int age;
    private double height;

    @ManyToOne
    @JoinColumn(name = "breed")
    private Breed breed;
}
