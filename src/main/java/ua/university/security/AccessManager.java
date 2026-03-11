package ua.university.security;

import ua.university.domain.enums.Role;
import ua.university.exception.AccessDeniedException;

import java.util.Set;

public class AccessManager {

    public void requireAnyRole(User user, Set<Role> allowedRoles) {
        if (user == null) {
            throw new AccessDeniedException("User is not authenticated");
        }
        if (!allowedRoles.contains(user.role())) {
            throw new AccessDeniedException(
                    "Access denied for role " + user.role() + ". Allowed roles: " + allowedRoles
            );
        }
    }
}
