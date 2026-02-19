package com.uniovi.sdi.grademanager.controllers;

import com.uniovi.sdi.grademanager.entities.Department;
import com.uniovi.sdi.grademanager.services.DepartmentsService;
import com.uniovi.sdi.grademanager.validators.DepartmentAddFormValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentsService departmentsService;
    private final DepartmentAddFormValidator departmentAddFormValidator;

    public DepartmentController(
            DepartmentsService departmentsService,
            DepartmentAddFormValidator departmentAddFormValidator) {
        this.departmentsService = departmentsService;
        this.departmentAddFormValidator = departmentAddFormValidator;
    }

    // LISTAR
    @GetMapping
    public String list(Model model) {
        model.addAttribute("departmentsList",
                departmentsService.getDepartments());
        return "departments/list";
    }

    // DETALLE
    @GetMapping("/details/{id}")
    public String details(@PathVariable Long id, Model model) {

        model.addAttribute("department",
                departmentsService.getDepartment(id));

        return "departments/details";
    }


    // FORMULARIO AÑADIR
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("department", new Department());
        return "departments/add";
    }

    // este valida en servidor y si falla vuelve al add departments
    @PostMapping("/add")
    public String addPost(@Validated @ModelAttribute("department") Department department, BindingResult result) {
        departmentAddFormValidator.validate(department, result);
        if (result.hasErrors()) {
            return "departments/add";
        }
        departmentsService.addDepartment(department);
        return "redirect:/departments";
    }

    // FORMULARIO EDITAR
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("department",
                departmentsService.getDepartment(id));
        return "departments/edit";
    }

    // EDITAR (POST)
    @PostMapping("/edit/{id}")
    public String editPost(@PathVariable Long id,
                           @ModelAttribute Department department) {

        departmentsService.updateDepartment(id, department);
        return "redirect:/departments/details/" + id;
    }

    // BORRAR
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        departmentsService.deleteDepartment(id);
        return "redirect:/departments";
    }
}
