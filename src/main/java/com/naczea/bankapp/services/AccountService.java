package com.naczea.bankapp.services;

import com.naczea.bankapp.entities.Account;

import java.util.List;

public interface AccountService {
    Account findById(Long id);
    Account findByAccountNumber(Long accountNumber);
    List<Account> findByClientIdentification(String clientIdentification);
    Account saveAccount(Account account);
    void deleteAccount(Account account);
}
