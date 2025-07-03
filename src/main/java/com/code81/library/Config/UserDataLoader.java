package com.code81.library.Config;

import com.code81.library.DTO.UserDTO;
import com.code81.library.Entity.Role;
import com.code81.library.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserDataLoader {

    private final UserService userService;
    public UserDataLoader(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public CommandLineRunner loadUsers() {
        return args -> {
            UserDTO user1 = UserDTO.builder()
                    .firstName("Admin")
                    .lastName("User")
                    .username("admin.user")
                    .email("admin@example.com")
                    .password("admin123")
                    .role(Role.ADMINISTRATOR)
                    .nid("11112222333344")
                    .phone("01000000000")
                    .address("Admin Address")
                    .build();

            UserDTO user2 = UserDTO.builder()
                    .firstName("Librarian")
                    .lastName("User")
                    .username("librarian.user")
                    .email("librarian@example.com")
                    .password("lib12345")
                    .role(Role.LIBRARIAN)
                    .nid("22223333444455")
                    .phone("01111111111")
                    .address("Librarian Address")
                    .build();

            UserDTO user3 = UserDTO.builder()
                    .firstName("Staff")
                    .lastName("User")
                    .username("staff.user")
                    .email("staff@example.com")
                    .password("staff1234")
                    .role(Role.STAFF)
                    .nid("33334444555566")
                    .phone("01222222222")
                    .address("Staff Address")
                    .build();

            userService.createUser(user1);
            userService.createUser(user2);
            userService.createUser(user3);

            System.out.println("Users loaded successfully!");
        };
    }
}
