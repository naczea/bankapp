package com.naczea.bankapp.entities;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClientValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Successfully validates when creating a client with all fields")
    void shouldPassValidationWhenAllFieldsAreValid() {

        Client client = new Client();
        client.setName("Ana Armas");
        client.setGender("F");
        client.setAge(30);
        client.setIdentification("0123456789");
        client.setAddress("Calle A y Calle B");
        client.setPhoneNumber("555444");
        client.setPassword("password123");
        client.setState(true);

        Set<ConstraintViolation<Client>> violations = validator.validate(client);
        assertTrue(violations.isEmpty(), "Validate OK client with all fields");
    }

    @Test
    @DisplayName("Validation fails when creating a client with null required fields")
    void shouldFailValidationWhenRequiredFieldIsNull() {
        Client client = new Client();
        client.setName(null);

        Set<ConstraintViolation<Client>> violations = validator.validate(client);
        assertFalse(violations.isEmpty(), "Validate fails when a required field is null");
    }
}