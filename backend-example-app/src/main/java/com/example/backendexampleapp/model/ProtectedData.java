package com.example.backendexampleapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProtectedData {

    public ProtectedData(String name, String surname, Integer old, String gender, String insuranceNumber) {
        this.name = name;
        this.surname = surname;
        this.old = old;
        this.gender = gender;
        this.insuranceNumber = insuranceNumber;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    String surname;
    Integer old;
    String gender;
    String insuranceNumber;
}
