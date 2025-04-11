package com.naczea.bankapp.services;

import com.naczea.bankapp.entities.Client;
import com.naczea.bankapp.repositories.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    public ClientServiceImpl(ClientRepository clientRepository, PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Client findById(Long id) {
        Client client = clientRepository.findById(id).orElse(null);
        if(Objects.isNull(client)){
            throw new EntityNotFoundException("Cliente no encontrado");
        }
        return client;
    }

    public Client findByClientIdentification(String clientIdentification) {
        Client client = clientRepository.findByIdentification(clientIdentification);
        if(Objects.isNull(client)){
            throw new EntityNotFoundException("Cliente no encontrado");
        }
        return client;
    }

    public Client findByClientName(String clientName) {
        Client client = clientRepository.findByName(clientName);
        if(Objects.isNull(client)){
            throw new EntityNotFoundException("Cliente no encontrado");
        }
        return client;
    }

    public void deleteClient(Client client) {
        clientRepository.delete(client);
    }

    @Override
    @Transactional
    public Client saveClient(Client client){
        try {
            client.setPassword(passwordEncoder.encode(client.getPassword()));
            return clientRepository.save(client);
        }catch (Exception e){
            e.printStackTrace();
            throw new IllegalArgumentException("Problems in the creation of [Client]: " + e.getMessage());
        }
    }
}
