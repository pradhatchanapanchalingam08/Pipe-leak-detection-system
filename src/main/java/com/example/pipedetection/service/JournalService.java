package com.example.pipedetection.service;

import com.example.pipedetection.entity.Account;
import com.example.pipedetection.entity.JournalEntry;
import com.example.pipedetection.repository.JournalEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Very simple journal/ledger helper. Each business event (invoice, payment, bill,
 * sealant consumption, repair revenue) writes ONE journal line to the most relevant
 * account, with either the debit or the credit column filled in. This keeps the
 * accounting logic easy to follow while still respecting basic debit/credit rules
 * (assets & expenses grow with a debit, liabilities & income grow with a credit).
 */
@Service
public class JournalService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private AccountService accountService;

    public JournalEntry recordDebit(String accountName, BigDecimal amount, String description,
                                     String referenceType, Long referenceId) {
        Account account = accountService.getAccountByName(accountName);
        JournalEntry entry = new JournalEntry(LocalDate.now(), account.getId(), description,
                amount, BigDecimal.ZERO, referenceType, referenceId);
        return journalEntryRepository.save(entry);
    }

    public JournalEntry recordCredit(String accountName, BigDecimal amount, String description,
                                      String referenceType, Long referenceId) {
        Account account = accountService.getAccountByName(accountName);
        JournalEntry entry = new JournalEntry(LocalDate.now(), account.getId(), description,
                BigDecimal.ZERO, amount, referenceType, referenceId);
        return journalEntryRepository.save(entry);
    }

    public List<JournalEntry> getAllEntries() {
        return journalEntryRepository.findAll();
    }

    public List<JournalEntry> getEntriesForAccount(Long accountId) {
        return journalEntryRepository.findByAccountId(accountId);
    }
}
