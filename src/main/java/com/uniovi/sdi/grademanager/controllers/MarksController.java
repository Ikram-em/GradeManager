package com.uniovi.sdi.grademanager.controllers;

import com.uniovi.sdi.grademanager.entities.Mark;
import com.uniovi.sdi.grademanager.services.MarksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MarksController {

    @Autowired
    private MarksService marksService;

    @GetMapping("/mark/list")
    public String getList(Model model) {
        model.addAttribute("markList", marksService.getMarks());
        return "mark/list";
    }

    @GetMapping("/mark/details/{id}")
    public String getDetail(@PathVariable Long id, Model model) {
        model.addAttribute("mark", marksService.getMark(id));
        return "mark/details"; // más adelante
    }

    @PostMapping("/mark/add")
    public String addMark(@ModelAttribute Mark mark) {
        marksService.addMark(mark);
        return "redirect:/mark/list";
    }

    @GetMapping("/mark/add")
    public String getAddForm(Model model) {
        model.addAttribute("mark", new Mark());
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
        return "mark/edit";
    }


    @PostMapping("/mark/edit/{id}")
    public String setEdit(@ModelAttribute Mark mark, @PathVariable Long id) {
        mark.setId(id);
        marksService.addMark(mark);
        return "redirect:/mark/details/" + id;
    }

    @PostMapping("/mark/edit")
    public String editMark(@ModelAttribute Mark mark) {
        marksService.addMark(mark);
        return "redirect:/mark/details/" + mark.getId();
    }



}

