package com.code81.library.Mapper;

import com.code81.library.DTO.UserDTO;
import com.code81.library.Entity.User;

public class UserMapper {

    public static UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .fullName(user.getFullName())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .nid(user.getNid())
                .phone(user.getPhone())
                .address(user.getAddress())
                .build();
    }

    public static User toEntity(UserDTO dto) {
        return User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(dto.getPassword()) // ⚠️ should be encoded in service
                .role(dto.getRole())
                .nid(dto.getNid())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .build();
    }
}
