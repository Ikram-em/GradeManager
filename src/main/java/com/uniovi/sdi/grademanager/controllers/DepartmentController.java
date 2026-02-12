package com.uniovi.sdi.grademanager.controllers;

import com.uniovi.sdi.grademanager.entities.Department;
import com.uniovi.sdi.grademanager.services.DepartmentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    private DepartmentsService departmentsService;

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
    public String addForm() {
        return "departments/add";
    }

    // AÑADIR (POST)
    @PostMapping("/add")
    public String addPost(@ModelAttribute Department department) {
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
