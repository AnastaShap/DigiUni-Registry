package ua.university.security;

import org.junit.jupiter.api.Test;
import ua.university.domain.Teacher;
import ua.university.dto.Email;
import ua.university.dto.PhoneNumber;
import ua.university.service.multithreading.AutoSaveService;
import ua.university.ui.student.StudentCRUDMenu;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class SecurityLogicTest {
    // Створюємо фіктивний клас спеціально для тестування рефлексії
    static class DummyMenu {
        @RequiresPermission(Permissions.DELETE_DATA)
        public void deleteStudent() {
        }

        @RequiresPermission(Permissions.EDIT_DATA)
        public void editStudent() {
        }
    }

    // Валідація бітової маски (Permissions)
    @Test
    void testPermissionsBitmask() {
        Teacher teacher = new Teacher("T1", "L", "F", "M", LocalDate.now(), null, null, "P", "D", "T", LocalDate.now(), 1.0);

        assertEquals(0, teacher.getPermissionMask());

        // Додаємо VIEW (1) та EDIT (2)
        teacher.addPermissions(Permissions.VIEW_ALL);
        teacher.addPermissions(Permissions.EDIT_DATA);

        // Маска має бути 3 (1 | 2)
        assertEquals(3, teacher.getPermissionMask());
        assertTrue(teacher.hasPermission(Permissions.VIEW_ALL));
        assertTrue(teacher.hasPermission(Permissions.EDIT_DATA));
        assertFalse(teacher.hasPermission(Permissions.DELETE_DATA)); // 4
    }

    @Test
    void testReflectionAccessControl() {
        AuthService authService = new AuthService();

        // Використовуємо наш тестовий клас замість new Object()
        Object dummyMenu = new DummyMenu();

        Teacher admin = new Teacher("T1", "Admin", "Admin", "", LocalDate.of(1980, 1, 1),
                new Email("admin@ukma.edu.ua"), new PhoneNumber("+380501234567"),
                "Prof", "PhD", "Tit", LocalDate.now(), 1.0);

        // Даємо адміну право на видалення
        admin.addPermissions(Permissions.DELETE_DATA);

        // перевірка: чи є доступ до методу видалення (має бути true)
        boolean canDelete = authService.canExecute(dummyMenu, "deleteStudent", admin);
        assertTrue(canDelete, "Admin with DELETE_DATA bit should have access to deleteStudent");

        // перевірка: чи відмовить у доступі до методу редагування (має бути false, бо ми дали тільки DELETE_DATA)
        boolean canEdit = authService.canExecute(dummyMenu, "editStudent", admin);
        assertFalse(canEdit, "Admin without EDIT_DATA bit should NOT have access to editStudent");
    }
}
