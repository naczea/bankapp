package com.naczea.bankapp.services;

import com.naczea.bankapp.entities.Client;

public interface ClientService {
    Client findById(Long id);
    Client findByClientIdentification(String clientIdentification);
    Client findByClientName(String clientName);
    Client saveClient(Client client);
    void deleteClient(Client client);
}