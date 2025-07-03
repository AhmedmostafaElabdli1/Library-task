package com.code81.library.Mapper;

import com.code81.library.DTO.*;
import com.code81.library.Entity.*;

import java.util.*;

public class BookMapper {

    public static BookDTO toDTO(Book book) {
        return BookDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .language(book.getLanguage())
                .publicationYear(book.getPublicationYear())
                .isbn(book.getIsbn())
                .edition(book.getEdition())
                .summary(book.getSummary())
                .coverImages(book.getCoverImages())
                .totalQuantity(book.getTotalQuantity())
                .remainingQuantity(book.getRemainingQuantity())
                .borrowedCount(book.getBorrowedCount())

                // 💡 Related fields
                .categoryPath(buildCategoryPath(book.getCategory()))
                .authors(book.getAuthors().stream().map(Author::getName).toList())
                .publisherName(book.getPublisher().getName())
                .publisherEmail(book.getPublisher().getEmail())
                // 🕒 Audit Info
                .createdAt(book.getCreatedAt())
                .updatedAt(book.getUpdatedAt())
                .createdBy(book.getCreatedBy())
                .updatedBy(book.getUpdatedBy())

                .build();
    }

    public static String buildCategoryPath(Category category) {
        List<String> path = new ArrayList<>();
        while (category != null) {
            path.add(0, category.getName());
            category = category.getParentCategory();
        }
        return String.join(" > ", path);
    }
    public static Book toEntity(BookRequestDto dto) {
        return Book.builder()
                .title(dto.getTitle())
                .language(dto.getLanguage())
                .publicationYear(dto.getPublicationYear())
                .isbn(dto.getIsbn())
                .edition(dto.getEdition())
                .summary(dto.getSummary())
                .coverImages(dto.getCoverImages())
                .totalQuantity(dto.getTotalQuantity())
                .remainingQuantity(dto.getTotalQuantity())  // Initially, all available
                .borrowedCount(0)
                .build();
    }
}
