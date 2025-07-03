package com.code81.library.Service;

import com.code81.library.DTO.UserDTO;
import com.code81.library.Entity.Role;
import com.code81.library.Entity.User;
import com.code81.library.Exception.EmailAlreadyExistsException;
import com.code81.library.Exception.NidAlreadyExistsException;
import com.code81.library.Exception.PhoneAlreadyExistsException;
import com.code81.library.Mapper.UserMapper;
import com.code81.library.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    public UserDTO createUser(UserDTO dto) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new EmailAlreadyExistsException("Username already exists: " + dto.getEmail());
        }
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already used by another user : " + dto.getEmail());
        }
        if (userRepository.existsByPhone(dto.getPhone())) {
            throw new PhoneAlreadyExistsException("Phone already used by another user: " + dto.getPhone());
        }
        if (userRepository.existsByNid(dto.getNid())) {
            throw new NidAlreadyExistsException("You have Account with This NID : " + dto.getNid());
        }
        User entity = UserMapper.toEntity(dto);
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        entity.setEnabled(true);
        entity.setAccountNonLocked(true);
        entity.setAccountNonExpired(true);
        entity.setCredentialsNonExpired(true);
        return UserMapper.toDTO(userRepository.save(entity));
    }


    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toDTO)
                .toList();
    }

    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id)
                .map(UserMapper::toDTO);
    }


    public List<UserDTO> getUsersByRole(String roleName) {
        Role role;
        try {
            role = Role.valueOf(roleName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + roleName);
        }

        return userRepository.findAllByRole(role).stream()
                .map(UserMapper::toDTO)
                .toList();
    }

    public Optional<UserDTO> updateUser(Long id, User updated) {
        return userRepository.findById(id).map(existing -> {
            existing.setFirstName(updated.getFirstName());
            existing.setLastName(updated.getLastName());
            existing.setUsername(updated.getUsername());
            existing.setEmail(updated.getEmail());
            existing.setPassword(updated.getPassword());
            existing.setRole(updated.getRole());
            existing.setNid(updated.getNid());
            existing.setPhone(updated.getPhone());
            existing.setAddress(updated.getAddress());
            // 🔐 Only encode new password if it’s different
            if (!updated.getPassword().isBlank()) {
                existing.setPassword(passwordEncoder.encode(updated.getPassword()));
            }
            return UserMapper.toDTO(userRepository.save(existing));
        });
    }

    public boolean deleteUser(Long id) {
        if (!userRepository.existsById(id)) return false;
        userRepository.deleteById(id);
        return true;
    }
}

