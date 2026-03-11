package ua.university.repository;

import ua.university.domain.Department;
import ua.university.domain.Faculty;

public class InMemoryFacultyRepository extends InMemoryRepository<Faculty, String> {
    @Override
    public void save(Faculty faculty) {
        storage.put(faculty.getCode(), faculty);
    }
}
