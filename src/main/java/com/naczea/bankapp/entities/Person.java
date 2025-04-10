package com.naczea.bankapp.entities;

import com.naczea.bankapp.util.Identifiable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class Person implements Serializable, Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message ="The field [name-Person] is required")
    private String name;

    private String gender;
    private Integer age;
    private String identification;

    @NotNull(message ="The field [address-Person] is required")
    private String address;

    @NotNull(message ="The field [phoneNumber-Person] is required")
    private String phoneNumber;
}
