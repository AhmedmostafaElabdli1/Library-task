package com.code81.library.Service;

import com.code81.library.DTO.AuthorDTO;
import com.code81.library.Entity.Author;
import com.code81.library.Exception.EmailAlreadyExistsException;
import com.code81.library.Exception.ResourceNotFound;
import com.code81.library.Mapper.AuthorMapper;
import com.code81.library.Repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorDTO createAuthor(AuthorDTO dto) {
        if (authorRepository.existsByEmail(dto.getEmail())){
            throw  new EmailAlreadyExistsException("Email already used by another Author : " + dto.getEmail()+" or Author added before");
        }
        Author author = AuthorMapper.toEntity(dto);
        return AuthorMapper.toDTO(authorRepository.save(author));
    }

    public List<AuthorDTO> getAllAuthors() {
        return authorRepository.findAll()
                .stream()
                .map(AuthorMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<AuthorDTO> getAuthorById(Long id) {
        return authorRepository.findById(id).map(AuthorMapper::toDTO);
    }

    public Optional<AuthorDTO> updateAuthor(Long id, AuthorDTO dto) {
        if (!authorRepository.existsById(id)) {
            throw new ResourceNotFound("Author for this ID :"+ id +" not found, Please Create Author First");
        }
        return authorRepository.findById(id).map(existing -> {
            existing.setName(dto.getName());
            existing.setBio(dto.getBio());
            existing.setNationality(dto.getNationality());
            return AuthorMapper.toDTO(authorRepository.save(existing));
        });
    }

    public boolean deleteAuthor(String Email) {
        if (!authorRepository.existsByEmail(Email)){
            throw new ResourceNotFound("Author for this Email :"+ Email +" not found, Please Create Author First");
        }

        authorRepository.deleteByEmail(Email);
        return true;
    }

//    public Author getAuthorEntityById(Long id) {
//        return authorRepository.findById(id)
//                .orElseThrow(() -> new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Author not found with id: " + id));
//    }
}
