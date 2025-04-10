package com.naczea.bankapp.services;

import com.naczea.bankapp.entities.Client;
import com.naczea.bankapp.repositories.ClientRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    public ClientServiceImpl(ClientRepository clientRepository, PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Client findById(Long id) {
        return clientRepository.findById(id).orElse(null);
    }

    public Client findByClientIdentification(String clientIdentification) {
        return clientRepository.findByIdentification(clientIdentification);
    }

    public Client findByClientName(String clientName) {
        return clientRepository.findByName(clientName);
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
