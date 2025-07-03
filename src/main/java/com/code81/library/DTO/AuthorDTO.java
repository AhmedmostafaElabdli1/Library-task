package com.code81.library.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorDTO {
    private Long id;
    private String name;
    private String bio;
    private String nationality;
    @NotBlank(message = "Email is required")
    @Email(message = "Enter Valid Email")
    private String email;
}
