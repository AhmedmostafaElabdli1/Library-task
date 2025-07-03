package com.code81.library.DTO;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder

public class BookDTO {
    private Long id;
    private String title;
    private String language;
    private int publicationYear;
    private String isbn;
    private String edition;
    private String summary;
    private String categoryPath;
    private List<String> coverImages;
    private int totalQuantity;
    private int remainingQuantity;
    private int borrowedCount;

    private List<String> authors;     // Author names
    private String publisherName;     // Publisher details
    private String publisherEmail;
    // 🕒 Auditing
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
