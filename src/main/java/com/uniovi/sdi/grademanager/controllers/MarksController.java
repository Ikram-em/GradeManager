package com.uniovi.sdi.grademanager.controllers;

import com.uniovi.sdi.grademanager.entities.Mark;
import com.uniovi.sdi.grademanager.entities.User;
import com.uniovi.sdi.grademanager.services.MarksService;
import com.uniovi.sdi.grademanager.services.UsersService;
import com.uniovi.sdi.grademanager.validators.MarkAddFormValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class MarksController {

    private final MarksService marksService;
    private final UsersService usersService;
    private final MarkAddFormValidator markAddFormValidator;

    public MarksController(
            MarksService marksService,
            UsersService usersService,
            MarkAddFormValidator markAddFormValidator) {
        this.marksService = marksService;
        this.usersService = usersService;
        this.markAddFormValidator = markAddFormValidator;
    }

    @GetMapping("/mark/list")
    public String getList(
            Model model,
            Pageable pageable,
            Principal principal,
            @RequestParam(value = "searchText", required = false) String searchText) {
        String dni = principal.getName();
        User user = usersService.getUserByDni(dni);
        Page<Mark> marks;
        if (searchText != null && !searchText.isBlank()) {
            marks = marksService.searchMarksByDescriptionAndNameForUser(pageable, searchText, user);
        } else {
            marks = marksService.getMarksForUser(pageable, user);
        }
        model.addAttribute("marksList", marks.getContent());
        model.addAttribute("page", marks);
        model.addAttribute("searchText", searchText);
        return "mark/list";
    }

    @GetMapping("/mark/list/update")
    public String updateList(
            Model model,
            Pageable pageable,
            Principal principal,
            @RequestParam(value = "searchText", required = false) String searchText) {
        String dni = principal.getName();
        User user = usersService.getUserByDni(dni);
        Page<Mark> marks;
        if (searchText != null && !searchText.isBlank()) {
            marks = marksService.searchMarksByDescriptionAndNameForUser(pageable, searchText, user);
        } else {
            marks = marksService.getMarksForUser(pageable, user);
        }
        model.addAttribute("marksList", marks.getContent());
        return "mark/list :: marksTable";
    }

    @GetMapping("/mark/details/{id}")
    public String getDetail(@PathVariable Long id, Model model) {
        Mark mark = marksService.getMark(id);
        if (mark == null) {
            return "redirect:/mark/list";
        }
        model.addAttribute("mark", mark);
        return "mark/details"; // más adelante
    }

    @PostMapping("/mark/add")
    public String addMark(
            @Validated @ModelAttribute("mark") Mark mark,
            BindingResult result,
            @RequestParam("user") Long userId,
            Model model) {
        markAddFormValidator.validate(mark, result);
        //si hay errores pues recargar
        if (result.hasErrors()) {
            model.addAttribute("usersList", usersService.getUsers());
            return "mark/add";
        }
        //si no ,guardar
        mark.setUser(usersService.getUser(userId));
        marksService.addMark(mark);
        return "redirect:/mark/list";
    }

    @GetMapping("/mark/add")
    public String getAddForm(Model model) {
        model.addAttribute("mark", new Mark());
        model.addAttribute("usersList", usersService.getUsers());
        return "mark/add";
    }

    @GetMapping("/mark/delete/{id}")
    public String deleteMark(@PathVariable Long id) {
        marksService.deleteMark(id);
        return "redirect:/mark/list";
    }


    @GetMapping("/mark/edit/{id}")
    public String getEdit(Model model, @PathVariable Long id) {
        model.addAttribute("mark", marksService.getMark(id));
        model.addAttribute("usersList", usersService.getUsers());
        return "mark/edit";
    }


    @PostMapping("/mark/edit/{id}")
    public String setEdit(@ModelAttribute Mark mark, @PathVariable Long id) {
        Mark originalMark = marksService.getMark(id);
        if (originalMark == null) {
            return "redirect:/mark/list";
        }
        originalMark.setScore(mark.getScore());
        originalMark.setDescription(mark.getDescription());
        marksService.addMark(originalMark);
        return "redirect:/mark/details/" + id;
    }

    @GetMapping("/mark/{id}/resend")
    public String setResendTrue(@PathVariable Long id) {
        marksService.setMarkResend(true, id);
        return "redirect:/mark/list";
    }

    @GetMapping("/mark/{id}/noresend")
    public String setResendFalse(@PathVariable Long id) {
        marksService.setMarkResend(false, id);
        return "redirect:/mark/list";
    }
}
