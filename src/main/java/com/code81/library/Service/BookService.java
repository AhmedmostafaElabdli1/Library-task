package com.code81.library.Service;

import com.code81.library.Entity.Author;
import com.code81.library.Entity.Book;
import com.code81.library.Entity.Category;
import com.code81.library.Entity.Publisher;
import com.code81.library.Exception.ResourceNotFound;
import com.code81.library.Mapper.BookMapper;
import com.code81.library.Repository.AuthorRepository;
import com.code81.library.Repository.BookRepository;
import com.code81.library.Repository.CategoryRepository;
import com.code81.library.Repository.PublisherRepository;
import com.code81.library.DTO.BookDTO;
import com.code81.library.DTO.BookRequestDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BookService {


    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;
    @Autowired
    private CategoryRepository categoryRepository;


    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(BookMapper::toDTO)
                .toList();
    }

    public Optional<BookDTO> getBookById(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFound("Book Not Found By id :" +id);
        }
        return bookRepository.findById(id)
                .map(BookMapper::toDTO);
    }

    @Transactional
    public BookDTO createBook(BookRequestDto dto) {
        Book book = BookMapper.toEntity(dto);

        // Set authors
        List<Author> authors = authorRepository.findAllById(dto.getAuthorIds());
        if (authors.size() != dto.getAuthorIds().size()) {
            throw new IllegalArgumentException("One or more authors not found.");
        }
        book.setAuthors(new HashSet<>(authors));

        // Set publisher
        Publisher publisher = publisherRepository.findById(dto.getPublisherId())
                .orElseThrow(() -> new IllegalArgumentException("Publisher not found."));
        book.setPublisher(publisher);
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        book.setCategory(category);
        return BookMapper.toDTO(bookRepository.save(book));
    }

    @Transactional
    public BookDTO updateBook(Long id, BookRequestDto dto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        // Update fields
        book.setTitle(dto.getTitle());
        book.setLanguage(dto.getLanguage());
        book.setPublicationYear(dto.getPublicationYear());
        book.setIsbn(dto.getIsbn());
        book.setEdition(dto.getEdition());
        book.setSummary(dto.getSummary());
        book.setCoverImages(dto.getCoverImages());
        book.setTotalQuantity(dto.getTotalQuantity());
        book.setRemainingQuantity(dto.getTotalQuantity());

        // Set authors
        List<Author> authors = authorRepository.findAllById(dto.getAuthorIds());
        if (authors.size() != dto.getAuthorIds().size()) {
            throw new IllegalArgumentException("Invalid author IDs.");
        }
        book.setAuthors(new HashSet<>(authors));

        // Set publisher
        Publisher publisher = publisherRepository.findById(dto.getPublisherId())
                .orElseThrow(() -> new IllegalArgumentException("Publisher not found."));
        book.setPublisher(publisher);

        return BookMapper.toDTO(bookRepository.save(book));
    }

    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Book not found");
        }
        bookRepository.deleteById(id);
    }

    public List<Book> getBooksByAuthorId(Long authorId) {
        return bookRepository.findByAuthors_Id(authorId);
    }

    public List<Book> getBooksByPublisherId(Long publisherId) {
        return bookRepository.findByPublisher_Id(publisherId);
    }
}
