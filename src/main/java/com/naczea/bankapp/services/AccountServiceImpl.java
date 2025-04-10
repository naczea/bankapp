package com.naczea.bankapp.services;

import com.naczea.bankapp.entities.Account;
import com.naczea.bankapp.repositories.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account findById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }
    public Account findByAccountNumber(Long accountNumber) {
        return accountRepository.findByNumber(accountNumber);
    }

    public List<Account> findByClientIdentification(String clientIdentification) {
        return accountRepository.findByClientIdentification(clientIdentification);
    }

    public void deleteAccount(Account account) {
        accountRepository.delete(account);
    }

    @Override
    @Transactional
    public Account saveAccount(Account account){
        try {
            return accountRepository.save(account);
        }catch (Exception e){
            e.printStackTrace();
            throw new IllegalArgumentException("Problems in the creation of [Account]: " + e.getMessage());
        }
    }
}
