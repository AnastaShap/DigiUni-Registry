package ua.university.security;

import ua.university.domain.enums.Role;

public record User(String login, Role role) {}
