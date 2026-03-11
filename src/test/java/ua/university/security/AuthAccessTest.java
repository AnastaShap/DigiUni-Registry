package ua.university.security;

import org.junit.jupiter.api.Test;
import ua.university.domain.enums.Role;
import ua.university.exception.AccessDeniedException;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AuthAccessTest {

    @Test
    void adminLoginShouldReturnManagerRole() {
        AuthService authService = new AuthService();

        User user = authService.login("admin");

        assertEquals(Role.MANAGER, user.role());
    }

    @Test
    void regularUserShouldNotAccessManagerOnlyAction() {
        AccessManager accessManager = new AccessManager();
        User user = new User("student", Role.USER);

        assertThrows(AccessDeniedException.class,
                () -> accessManager.requireAnyRole(user, Set.of(Role.MANAGER)));
    }
}
