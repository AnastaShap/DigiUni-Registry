package ua.university.security;

import ua.university.domain.enums.Role;

public class AuthService {

    public User login(String login) {
        if ("admin".equals(login))
            return new User(login, Role.MANAGER);

        return new User(login, Role.USER);
    }
}
