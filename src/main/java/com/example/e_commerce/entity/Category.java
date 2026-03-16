package com.example.e_commerce.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "category",
        indexes = {
                @Index(name = "idx_category_name", columnList = "name"),

        }
)
public class Category {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@Column(nullable = false,unique = true)
private String name;

private String description;


@OneToMany(mappedBy = "category",cascade = CascadeType.ALL,orphanRemoval = true)
@JsonManagedReference
private List<Product> products;

public Category(String name){}
}
