package com.naczea.bankapp.repositories;

import com.naczea.bankapp.entities.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AccountRepository extends CrudRepository<Account, Long> {
    Account findByNumber(Long number);
    List<Account> findByClientIdentification(String clientIdentification);
}
