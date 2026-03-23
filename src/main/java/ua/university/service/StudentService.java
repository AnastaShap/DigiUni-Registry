package ua.university.service;

import ua.university.domain.Department;
import ua.university.domain.Student;
import ua.university.exception.DuplicateEntityException;
import ua.university.exception.StudentNotFoundException;
import ua.university.repository.student.IStudentRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Сервісний клас для роботи зі студентами.
 * <p>
 * Містить бізнес-логіку пошуку та формування звітів.Використовує Stream API
 * Не залежить від конкретного способу зберігання даних.
 * </p>
 */

public class StudentService {

    private final IStudentRepository repository;
    /**
     * Створює сервіс студентів.
     * @param repository репозиторій студентів
     */
    public StudentService(IStudentRepository repository) {
        this.repository = repository;
    }

    // --- CRUD ---
    public void create(Student student) {
        // Перевірка, чи вже існує студент з таким ID
        if (repository.findById(student.getId()).isPresent()) {
            throw new DuplicateEntityException("Студент з ID " + student.getId() + " вже існує!");
        }
        repository.save(student);
    }

    // helper for show()
    public Optional<Student> findById(String id) {
        return repository.findById(id);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }

    public void update(Student student) {
        repository.save(student); // upsert
    }

    // ===== SEARCH =====


    // Generalised search using Predicate
    public List<Student> findBy(Predicate<Student> filter) {
        return repository.findAll()
                .stream()
                .filter(filter)
                .toList();
    }
    /* Example of usage(lambda):
    findBy(s -> s.getCourse() == course);
    findBy(s -> s.getGroup().equalsIgnoreCase(group)); */

    /**
     * Пошук студентів за повним іменем (часткове співпадіння).
     * @param query рядок для пошуку
     * @return список студентів, що відповідають запиту
     */
    public List<Student> findByFullName(String query) {
        return repository.findAll().stream()
                .filter(s -> s.getFullName()
                        .toLowerCase()
                        .contains(query.toLowerCase()))
                .toList();
    }

    /**
     * Пошук студентів за курсом.
     * @param course номер курсу
     */
    public List<Student> findByCourse(int course) {
       /* return repository.findAll().stream()
                .filter(s -> s.getCourse() == course)
                .toList();*/

       return findBy(s -> s.getCourse() == course);  // LAMBDA USAGE
    }

    /**
     * Пошук студентів за групою.
     * @param group назва групи
     * @return список студентів цієї групи
     */
    public List<Student> findByGroup(String group) {
        return repository.findAll().stream()
                .filter(s -> s.getGroup().equalsIgnoreCase(group))
                .toList();
    }


     //Finds all students belonging to a specific department.


    public List<Student> findByDepartment(String departmentId) {
        return repository.findAll().stream()
                .filter(s -> s.getDepartment().equals(departmentId))
                .toList();
    }

    // ===== REPORTS =====

    /**
     * Формує список усіх студентів, відсортований за курсом.
     * @return відсортований список студентів
     */
    public List<Student> getStudentsSortedByCourse() {
        return repository.findAll().stream()
                .sorted(Comparator.comparingInt(Student::getCourse))
                .toList();
    }

    /**
     * Формує список усіх студентів, відсортований за алфавітом.
     * @return відсортований список студентів
     */
    public List<Student> getStudentsSortedByName() {
        return repository.findAll().stream()
                .sorted(Comparator.comparing(Student::getLastName)
                        .thenComparing(Student::getFirstName))
                .toList();
    }

    public Student getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    public List<Student> getAllStudents() {
        return repository.findAll();
    }

    // EDIT
    public void changeCourse(String id, int newCourse) {

        Student student = repository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));

        student.setCourse(newCourse);

        repository.save(student);
    }

    public void changeGroup(String id, String newGroup) {

        Student student = repository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));

        student.setGroup(newGroup);

        repository.save(student);
    }

    public void changeName(String id, String lastName, String firstName, String middleName) {
        Student student = repository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));

        student.setLastName(lastName);
        student.setFirstName(firstName);
        student.setMiddleName(middleName);

        repository.save(student);
    }
    public void transferToDepartment(String studentId, Department newDepartment) {
        Student student = repository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));

        student.setDepartment(newDepartment); // Business logic for transfer
        repository.save(student);
    }

    public int calculateAge(Student student) {
        if (student.getBirthDate() == null) {
            return 0;
        }
        return Period.between(student.getBirthDate(), LocalDate.now()).getYears();
    }

}
