package ru.practicum.categories.model;

import lombok.*;

import javax.persistence.*;

@Table(name = "categories")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column
    private String name;
}
