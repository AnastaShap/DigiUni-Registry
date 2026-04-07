
/// == LEGACY CLASS FOR STUDENT REPO ==

package ua.university.repository.student;

import ua.university.domain.Student;
import ua.university.repository.IRepository;

/**
 * Репозиторій для роботи зі студентами.
 * <p>
 * Визначає базові операції доступу до даних.
 * </p>
 */
public interface IStudentRepository extends IRepository<Student, String> {

    // специфічні методи для студентів,
    // якщо такі будуть (наприклад, знайти за групою).
    // Базові CRUD методи вже є в IRepository.
}
