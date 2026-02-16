package com.uniovi.sdi.grademanager.services;

import com.uniovi.sdi.grademanager.entities.Mark;
import com.uniovi.sdi.grademanager.repositories.MarksRepository;
import com.uniovi.sdi.grademanager.repositories.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MarksService {

    private final MarksRepository marksRepository;
    private final UsersRepository usersRepository;

    public MarksService(MarksRepository marksRepository, UsersRepository usersRepository) {
        this.marksRepository = marksRepository;
        this.usersRepository = usersRepository;
    }

    public List<Mark> getMarks() {
        List<Mark> marks = new ArrayList<>();
        marksRepository.findAll().forEach(marks::add);
        return marks;
    }

    public Mark getMark(Long id) {
        return marksRepository.findById(id).orElse(null);
    }

    public void addMark(Mark mark) {
        if (mark.getUser() != null && mark.getUser().getId() != null) {
            usersRepository.findById(mark.getUser().getId()).ifPresent(mark::setUser);
        }
        marksRepository.save(mark);
    }

    public void deleteMark(Long id) {
        marksRepository.deleteById(id);
    }

    public List<Mark> getMarksByUserDni(String dni) {
        return marksRepository.findAllByUserDni(dni);
    }
}
