package ua.university.security;

import ua.university.domain.enums.Role;
import ua.university.exception.AccessDeniedException;

import java.util.Set;

public class AccessManager {

    public void requireAnyRole(User user, Set<Role> allowedRoles) {
        if (user == null) {
            throw new AccessDeniedException("Користувач не авторизований");
        }

        if (!allowedRoles.contains(user.getRole())) {
            throw new AccessDeniedException(
                    "Доступ заборонено для ролі " + user.getRole() +
                            ". Дозволено: " + allowedRoles
            );
        }
    }
}