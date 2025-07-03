package com.code81.library.Entity;


import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

public enum Role {
    STAFF("Staff"),
    LIBRARIAN("Librarian"),
    ADMINISTRATOR("Admin");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    // JPA Converter
    @Converter(autoApply = true)
    public static class RoleConverter implements AttributeConverter<Role, String> {
        @Override
        public String convertToDatabaseColumn(Role role) {
            return role != null ? role.name() : null;
        }

        @Override
        public Role convertToEntityAttribute(String dbData) {
            return dbData != null ? Role.valueOf(dbData) : null;
        }
    }
}