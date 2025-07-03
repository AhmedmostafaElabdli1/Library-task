package com.code81.library.DTO;
import lombok.Data;
import java.util.List;

@Data
public class BookRequestDto {
    private String title;
    private String language;
    private int publicationYear;
    private String isbn;
    private String edition;
    private String summary;
    private List<String> coverImages;
    private int totalQuantity;

    private List<Long> authorIds;
    private Long publisherId;
    private Long categoryId;         // NEW: Add category ID

}
