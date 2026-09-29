package com.example.pipedetection.repository;

import com.example.pipedetection.entity.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {
    List<JournalEntry> findByAccountId(Long accountId);
}
