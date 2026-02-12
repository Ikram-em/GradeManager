package com.uniovi.sdi.grademanager.controllers;

import com.uniovi.sdi.grademanager.entities.Department;
import com.uniovi.sdi.grademanager.services.DepartmentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    private DepartmentsService departmentsService;

    @GetMapping
    public String list() {
        List<Department> departments = departmentsService.getDepartments();
        return "Listado de departamentos: " + departments;
    }

    @GetMapping("/details")
    public String details(@RequestParam Long id) {
        Department department = departmentsService.getDepartment(id);
        return "Detalle de departamento: " + department;
    }

    @GetMapping("/add")
    public String add() {
        return "Formulario para añadir departamento";
    }

    // ---------------- AÑADIR (POST) ----------------
    @PostMapping("/add")
    public String addPost(@RequestBody Department department) {
        departmentsService.addDepartment(department);
        return "Departamento añadido correctamente: " + department;
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id) {
        return "Editando departamento con id: " + id;
    }

    @PostMapping("/edit/{id}")
    public String editPost(@PathVariable Long id,
                           @RequestBody Department department) {

        departmentsService.updateDepartment(id, department);
        return "Departamento con id " + id + " actualizado";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        departmentsService.deleteDepartment(id);
        return "Departamento con id " + id + " eliminado";
    }
}
