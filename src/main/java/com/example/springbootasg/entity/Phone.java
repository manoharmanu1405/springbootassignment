package com.example.springbootasg.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table
@NoArgsConstructor
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Generated
    private Long id;

    @Column
    private String name;

    @Column
    private String price;

    public Phone(String name, String price) {
        this.name=name;
        this.price=price;
    }
}
