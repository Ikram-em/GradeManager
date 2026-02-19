package com.uniovi.sdi.grademanager.services;

import com.uniovi.sdi.grademanager.entities.Mark;
import com.uniovi.sdi.grademanager.repositories.MarksRepository;
import com.uniovi.sdi.grademanager.repositories.UsersRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class MarksService {

    private static final String CONSULTED_LIST_SESSION_KEY = "consultedList";

    private final MarksRepository marksRepository;
    private final UsersRepository usersRepository;
    private final HttpSession httpSession;

    public MarksService(MarksRepository marksRepository, UsersRepository usersRepository, HttpSession httpSession) {
        this.marksRepository = marksRepository;
        this.usersRepository = usersRepository;
        this.httpSession = httpSession;
    }

    public List<Mark> getMarks() {
        List<Mark> marks = new ArrayList<>();
        marksRepository.findAll().forEach(marks::add);
        return marks;
    }

    public Mark getMark(Long id) {
        Set<Mark> consultedList = getConsultedMarksFromSession();
        Mark mark = marksRepository.findById(id).orElse(null);
        if (mark != null) {
            consultedList.add(mark);
            httpSession.setAttribute(CONSULTED_LIST_SESSION_KEY, consultedList);
        }
        return mark;
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

    public Set<Mark> getConsultedMarks() {
        return getConsultedMarksFromSession();
    }

    @SuppressWarnings("unchecked")
    private Set<Mark> getConsultedMarksFromSession() {
        Object attribute = httpSession.getAttribute(CONSULTED_LIST_SESSION_KEY);
        if (attribute instanceof Set<?>) {
            return new LinkedHashSet<>((Set<Mark>) attribute);
        }
        return new LinkedHashSet<>();
    }
}
