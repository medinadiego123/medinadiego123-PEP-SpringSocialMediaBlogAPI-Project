package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account register(Account account) {
        return accountRepository.save(account);
    }

    public Account login(Account account) {
        return accountRepository.findByUsernameAndPassword(
                account.getUsername(),
                account.getPassword()
        );
    }

    public Account findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    public boolean accountExists(Integer accountId) {
        return accountRepository.existsById(accountId);
    }

    public boolean isValidRegistration(Account account) {
        if (account == null) {
            return false;
        }

        if (account.getUsername() == null || account.getUsername().isBlank()) {
            return false;
        }

        if (account.getPassword() == null || account.getPassword().length() < 4) {
            return false;
        }

        return true;
    }
}