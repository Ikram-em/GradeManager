package com.uniovi.sdi.grademanager.services;

import com.uniovi.sdi.grademanager.entities.Mark;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class MarksService {

    private final List<Mark> marksList = new LinkedList<>();

    @PostConstruct
    public void init() {
        marksList.add(new Mark(1L, "Exercise 1", 10.0));
        marksList.add(new Mark(2L, "Exercise 2", 9.0));
    }

    public List<Mark> getMarks() {
        return marksList;
    }

    public Mark getMark(Long id) {
        return marksList.stream()
                .filter(mark -> mark.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void addMark(Mark mark) {
        // Si el id es null, asignamos el último + 1
        if (mark.getId() == null && !marksList.isEmpty()) {
            mark.setId(marksList.getLast().getId() + 1);
        }
        marksList.add(mark);
    }

    public void deleteMark(Long id) {
        marksList.removeIf(mark -> mark.getId().equals(id));
    }
}
