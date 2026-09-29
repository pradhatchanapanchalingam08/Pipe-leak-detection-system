package com.example.pipedetection.service;

import com.example.pipedetection.entity.Contact;
import com.example.pipedetection.exception.ResourceNotFoundException;
import com.example.pipedetection.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    public Contact createContact(Contact contact) {
        return contactRepository.save(contact);
    }

    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    public List<Contact> getContactsByType(Contact.ContactType type) {
        return contactRepository.findByContactType(type);
    }

    public Contact getContactById(Long id) {
        return contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found with id: " + id));
    }

    public Contact updateContact(Long id, Contact updated) {
        Contact existing = getContactById(id);
        existing.setName(updated.getName());
        existing.setContactType(updated.getContactType());
        existing.setEmail(updated.getEmail());
        existing.setPhone(updated.getPhone());
        existing.setAddress(updated.getAddress());
        return contactRepository.save(existing);
    }

    public void deleteContact(Long id) {
        Contact existing = getContactById(id);
        contactRepository.delete(existing);
    }
}
