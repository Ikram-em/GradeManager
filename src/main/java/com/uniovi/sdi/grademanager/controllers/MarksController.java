package com.uniovi.sdi.grademanager.controllers;

import com.uniovi.sdi.grademanager.entities.Mark;
import org.springframework.web.bind.annotation.*;

@RestController
public class MarksController {

    @GetMapping("/mark/list")
    public String getList() {
        return "Getting List";
    }

    @GetMapping("/mark/details/{id}")
    public String getDetail(@PathVariable Long id) {
        return "Getting Details => " + id;
    }

    @PostMapping("/mark/add")
    public String setMark(@ModelAttribute Mark mark) {
        return "Added: " + mark.getDescription()
                + " with score: " + mark.getScore()
                + " id: " + mark.getId();
    }
}
