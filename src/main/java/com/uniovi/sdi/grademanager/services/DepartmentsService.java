package com.uniovi.sdi.grademanager.services;

import com.uniovi.sdi.grademanager.entities.Department;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class DepartmentsService {

    private final List<Department> departmentsList = new LinkedList<>();


    @PostConstruct
    public void init() {
        departmentsList.add(new Department(1L, "INF-01",
                "Departamento de Informática",
                "EII", 120));

        departmentsList.add(new Department(2L, "MAT-02",
                "Departamento de Matemáticas",
                "Facultad de Ciencias", 80));
    }

    public List<Department> getDepartments() {
        return departmentsList;
    }

    public Department getDepartment(Long id) {
        return departmentsList.stream()
                .filter(d -> d.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public boolean existsByCode(String code) {
        return departmentsList.stream()
                .anyMatch(d -> d.getCode() != null && d.getCode().equalsIgnoreCase(code));
    }

    public void addDepartment(Department department) {

        if (department.getId() == null) {
            if (departmentsList.isEmpty()) {
                department.setId(1L);
            } else {
                Long lastId = departmentsList.get(departmentsList.size() - 1).getId();
                department.setId(lastId + 1);
            }
        }

        departmentsList.add(department);
    }


    public void updateDepartment(Long id, Department updated) {

        Department existing = getDepartment(id);

        if (existing != null) {
            existing.setCode(updated.getCode());
            existing.setName(updated.getName());
            existing.setFaculty(updated.getFaculty());
            existing.setProfessors(updated.getProfessors());
        }
    }

    public void deleteDepartment(Long id) {
        departmentsList.removeIf(d -> d.getId().equals(id));
    }
}
