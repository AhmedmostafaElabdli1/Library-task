package com.code81.library.Service;

import com.code81.library.Entity.Publisher;
import com.code81.library.Exception.EmailAlreadyExistsException;
import com.code81.library.Exception.ResourceNotFound;
import com.code81.library.Repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublisherService {

    private final PublisherRepository publisherRepository;

    @Autowired
    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public Publisher createPublisher(Publisher publisher) {
        if (publisherRepository.existsByEmail(publisher.getEmail())) {
            throw new EmailAlreadyExistsException("Email already used by another Publisher : " + publisher.getEmail());
        }
        return publisherRepository.save(publisher);
    }

    public Publisher getPublisherById(Long id) {
        return publisherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Publisher not found with id: " + id));
    }
    public Publisher getPublisherByEmail(String email) {
        return publisherRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFound("Publisher not found with id: " + email));
    }

    public List<Publisher> getAllPublishers() {
        return publisherRepository.findAll();
    }

    public Publisher updatePublisher(String email, Publisher updatedPublisher) {
        if (!publisherRepository.existsByEmail(email)) {
            throw new ResourceNotFound("Publisher not found with email: " + email +" Please Create it First");
        }
        Publisher existing = getPublisherByEmail(email);
        existing.setName(updatedPublisher.getName());
        return publisherRepository.save(existing);
    }

    public void deletePublisher(Long id) {
        if (!publisherRepository.existsById(id)) {
            throw new RuntimeException("Publisher not found with id: " + id);
        }
        publisherRepository.deleteById(id);
    }
}
