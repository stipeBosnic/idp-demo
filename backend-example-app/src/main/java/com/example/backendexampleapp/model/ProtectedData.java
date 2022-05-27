package com.example.backendexampleapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProtectedData {

    String name;
    String surname;
    Integer old;
    String gender;
    @Id
    String insuranceNumber;
}
