package com.naczea.bankapp.repositories;

import com.naczea.bankapp.entities.Client;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<Client, Long> {
    Client findByIdentification(String identification);
    Client findByName(String name);
}