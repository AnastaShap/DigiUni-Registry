package ua.university.repository;

import ua.university.domain.Department;

public class InMemoryDepartmentRepository extends InMemoryRepository<Department, String> {
    @Override
    public void save(Department department) {
        storage.put(department.getCode(), department);
    }
}