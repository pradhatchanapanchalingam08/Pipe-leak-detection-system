package com.example.pipedetection.repository;

import com.example.pipedetection.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findByContactType(Contact.ContactType contactType);
}
