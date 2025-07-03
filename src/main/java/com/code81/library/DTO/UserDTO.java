package com.code81.library.DTO;

import com.code81.library.Entity.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private Long id;
    private String firstName; // ✅ for input
    private String lastName;  // ✅ for input
    private String fullName;  // for output only
    private String username;
    private String email;
    private String password;  // ✅ needed for user creation
    private Role role;
    private String nid;
    private String phone;
    private String address;
}
