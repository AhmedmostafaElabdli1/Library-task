package com.code81.library.Mapper;

import com.code81.library.DTO.AuthorDTO;
import com.code81.library.Entity.Author;

public class AuthorMapper {

    public static AuthorDTO toDTO(Author author) {
        return AuthorDTO.builder()
                .id(author.getId())
                .name(author.getName())
                .bio(author.getBio())
                .nationality(author.getNationality())
                .email(author.getEmail())
                .build();
    }

    public static Author toEntity(AuthorDTO dto) {
        return Author.builder()
                .id(dto.getId())
                .name(dto.getName())
                .bio(dto.getBio())
                .nationality(dto.getNationality())
                .email(dto.getEmail())
                .build();
    }
}
