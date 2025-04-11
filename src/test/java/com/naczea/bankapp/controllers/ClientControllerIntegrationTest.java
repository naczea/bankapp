package com.naczea.bankapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naczea.bankapp.entities.Client;
import com.naczea.bankapp.repositories.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ClientControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BASE_URL = "/clientes";
    private Client savedClient;

    @BeforeEach
    void setup() {
        clientRepository.deleteAll();
        Client client = new Client();
        client.setName("Carlos Estevez");
        client.setIdentification("1234567890");
        client.setAddress("Calle A y Calle B");
        client.setPhoneNumber("099999999");
        client.setPassword("1234");
        client.setState(true);
        client.setAge(30);
        client.setGender("M");

        savedClient = clientRepository.save(client);
    }

    @Nested
    @DisplayName("GET /clientByIdentification")
    class GetClientByIdentificationTests {

        @Test
        @DisplayName("Return valid client with existing Identification")
        void shouldReturnClientByIdentification() throws Exception {
            mockMvc.perform(get(BASE_URL + "/clientByIdentification")
                            .param("clientIdentification", savedClient.getIdentification())
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.estado").value("OK"))
                    .andExpect(jsonPath("$.payload.identification").value("1234567890"));
        }

        @Test
        @DisplayName("Return NOT_FOUND with non-existent Identification")
        void shouldReturnNotFoundIfClientDoesNotExist() throws Exception {
            mockMvc.perform(get(BASE_URL + "/clientByIdentification")
                            .param("clientIdentification", "0000000000"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("POST /newCient")
    class CreateClientTests {

        @Test
        @DisplayName("Create client with all fields")
        void shouldCreateNewClient() throws Exception {
            Client client = new Client();
            client.setName("karina Perez");
            client.setIdentification("0987654321");
            client.setAddress("Calle C y Calle D");
            client.setPhoneNumber("0999999999");
            client.setPassword("4321");
            client.setState(true);
            client.setAge(40);
            client.setGender("F");

            mockMvc.perform(post(BASE_URL + "/newCient")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(client)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.estado").value("OK"))
                    .andExpect(jsonPath("$.payload.name").value("karina Perez"));
        }
    }

    @Nested
    @DisplayName("DELETE /deleteClient")
    class DeleteClientTests {

        @Test
        @DisplayName("Delete existing client")
        void shouldDeleteClient() throws Exception {
            mockMvc.perform(delete(BASE_URL + "/deleteClient/" + savedClient.getId()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.estado").value("OK"));
        }

        @Test
        @DisplayName("Return NOT_FOUND with non-existing client")
        void shouldReturnNotFoundWhenDeletingNonExistingClient() throws Exception {
            mockMvc.perform(delete(BASE_URL + "/deleteClient/999"))
                    .andExpect(status().isNotFound());
        }
    }
}