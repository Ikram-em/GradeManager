package com.uniovi.sdi.grademanager.controllers;

import com.uniovi.sdi.grademanager.entities.Mark;
import com.uniovi.sdi.grademanager.services.MarksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MarksController {

    @Autowired
    private MarksService marksService;

    @GetMapping("/mark/list")
    public List<Mark> getList() {
        return marksService.getMarks();
    }

    @GetMapping("/mark/details/{id}")
    public Mark getDetail(@PathVariable Long id) {
        return marksService.getMark(id);
    }

    @PostMapping("/mark/add")
    public String addMark(@ModelAttribute Mark mark) {
        marksService.addMark(mark);
        return "Added: " + mark;
    }

    @DeleteMapping("/mark/delete/{id}")
    public String deleteMark(@PathVariable Long id) {
        marksService.deleteMark(id);
        return "Deleted mark with id: " + id;
    }
}
