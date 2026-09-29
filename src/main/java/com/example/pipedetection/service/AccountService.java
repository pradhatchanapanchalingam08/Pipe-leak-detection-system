package com.example.pipedetection.service;

import com.example.pipedetection.entity.Account;
import com.example.pipedetection.exception.ResourceNotFoundException;
import com.example.pipedetection.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
    }

    public Account getAccountByName(String name) {
        return accountRepository.findByAccountName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with name: " + name));
    }

    public Account updateAccount(Long id, Account updated) {
        Account existing = getAccountById(id);
        existing.setAccountName(updated.getAccountName());
        existing.setAccountType(updated.getAccountType());
        return accountRepository.save(existing);
    }

    public void deleteAccount(Long id) {
        Account existing = getAccountById(id);
        accountRepository.delete(existing);
    }
}
