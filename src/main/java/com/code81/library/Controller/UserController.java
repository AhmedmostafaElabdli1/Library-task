package com.code81.library.Controller;

import com.code81.library.DTO.UserDTO;
import com.code81.library.Entity.User;
import com.code81.library.Service.UserService;
import com.code81.library.Util.UserActivityLogger;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserActivityLogger userActivityLogger;

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userdto) {
        UserDTO created = userService.createUser(userdto);
        userActivityLogger.log("Created user: " + created.getUsername());
        return ResponseEntity.ok(created);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        userActivityLogger.log("Viewed all users");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(userDTO -> {
                    userActivityLogger.log("Viewed user with ID: " + id);
                    return ResponseEntity.ok(userDTO);
                })
                .orElseGet(() -> {
                    userActivityLogger.log("Attempted to view non-existing user with ID: " + id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserDTO>> getUsersByRole(@PathVariable("role") String roleName) {
        userActivityLogger.log("Viewed users with role: " + roleName);
        return ResponseEntity.ok(userService.getUsersByRole(roleName));
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user)
                .map(updated -> {
                    userActivityLogger.log("Updated user with ID: " + id);
                    return ResponseEntity.ok(updated);
                })
                .orElseGet(() -> {
                    userActivityLogger.log("Attempted to update non-existing user with ID: " + id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            userActivityLogger.log("Deleted user with ID: " + id);
            return ResponseEntity.noContent().build();
        } else {
            userActivityLogger.log("Attempted to delete non-existing user with ID: " + id);
            return ResponseEntity.notFound().build();
        }
    }
}
