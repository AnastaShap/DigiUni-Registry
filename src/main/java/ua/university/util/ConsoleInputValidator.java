package ua.university.util;

import java.util.Scanner;

/**
 * Утилітарний клас для валідації вводу з консолі.
 * <p>
 * Містить статичні методи для безпечного зчитування
 * даних від користувача.
 * </p>
 */

public class ConsoleInputValidator {

    /**
     * Зчитує непорожній рядок з консолі.
     * //@param message повідомлення для користувача
     * @param sc scanner для читання вводу
     * @return непорожній рядок
     */
    public static String readNonEmptyString(Scanner sc) {
        while (true) {
            //System.out.print(message);
            String input = sc.nextLine().trim();

            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Error: field cannot be empty.");
        }
    }

    public static int intInRange(Scanner sc, int min, int max) {
        int value;
        while (true) {
            try {
                value = Integer.parseInt(sc.nextLine());
                if (value < min || value > max)
                    System.out.println("Error: value must be between " + min + " and " + max);
                else return value;
            } catch (NumberFormatException e) {
                System.out.println("Error: enter a number.");
            }
        }
    }

    public static String readNumericId(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine().trim();
            // Перевірка: чи рядок складається тільки з цифр і не є порожнім
            if (input.matches("\\d+")) {
                return input;
            }
            System.out.println("Error: ID must contain only numbers. Please try again.");
        }
    }

    public static String readEmail(Scanner sc) {
        while (true) {
            System.out.print("Enter email: ");
            String input = sc.nextLine().trim();
            if (input.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
                return input;
            }
            System.out.println("Error: Invalid email format.");
        }
    }

    /**
     * Зчитує номер телефону у форматі +380... або 0...
     * @param sc Scanner для читання вводу
     * @return валідований номер телефону
     */
    public static String readPhone(Scanner sc) {
        while (true) {
            System.out.print("Enter phone (e.g., +380971234567): ");
            String input = sc.nextLine().trim();
            if (input.matches("^(\\+38)?0\\d{9}$")) {
                return input;
            } else {
                System.out.println("Error: Invalid phone format. Use +380XXXXXXXXX or 0XXXXXXXXX.");
            }
        }
    }

    // Курс 1–6
    /**
     * Зчитує номер курсу студента.
     * @return номер курсу (1–6)
     */
    public static int readCourse(Scanner sc) {
        while (true) {
            try {
                System.out.print("Enter course (1-6): ");
                int course = Integer.parseInt(sc.nextLine());

                if (course >= 1 && course <= 6) {
                    return course;
                }
                System.out.println("Course must be form 1 to 6.");
            } catch (NumberFormatException e) {
                System.out.println("Enter positive number, please.");
            }
        }
    }

    /**
     * Зчитує вибір пункту меню.
     * @param sc  scanner для читання вводу
     * @param min мінімально допустиме значення
     * @param max максимально допустиме значення
     * @return коректний вибір меню
     */
    public static int readMenuOption(Scanner sc, int min, int max) {
        while (true) {
            try {
                System.out.print("Your choice: ");
                int option = Integer.parseInt(sc.nextLine());

                if (option >= min && option <= max) {
                    return option;
                }
                System.out.printf("Enter number from %d to %d.%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Enter valid number, please.");
            }
        }
    }


    /**
     * Reads the group name following the specific pattern.
     * Pattern: 2-5 letters, a hyphen, and 1-2 digits.
     *
     * @param sc Scanner for input
     * @return validated group name
     */
    public static String readGroup(Scanner sc) {
        while (true) {
            System.out.print("Enter the group (e.g., ІПЗ-1): ");
            String input = sc.nextLine().trim();

            // Updated regex: \\d{1,2} allows both ІПЗ-1 and ІПЗ-11
            if (input.matches("^[A-Za-zА-Яа-яІіЇїЄє]{2,5}-\\d{1,2}$")) {
                return input;
            } else {
                System.out.println("Incorrect group format. Please use the format 'Letters-Number' (e.g., ІПЗ-1).");
            }
        }
    }



}
