package com.uniovi.sdi.grademanager.services;

import com.uniovi.sdi.grademanager.entities.Mark;
import com.uniovi.sdi.grademanager.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import com.uniovi.sdi.grademanager.repositories.MarksRepository;
import com.uniovi.sdi.grademanager.repositories.UsersRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
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

    public Page<Mark> getMarks(Pageable pageable) {
        return marksRepository.findAll(pageable);
    }

    public Page<Mark> getMarksForUser(Pageable pageable, User user) {
        if (user == null || user.getRole() == null) {
            return new PageImpl<>(new LinkedList<>(), pageable, 0);
        }
        if (user.getRole().equals("ROLE_STUDENT")) {
            return marksRepository.findAllByUser(pageable, user);
        }
        if (user.getRole().equals("ROLE_PROFESSOR") || user.getRole().equals("ROLE_ADMIN")) {
            return getMarks(pageable);
        }
        return new PageImpl<>(new LinkedList<>(), pageable, 0);
    }

    public Page<Mark> searchMarksByDescriptionAndNameForUser(Pageable pageable, String searchText, User user) {
        if (user == null || user.getRole() == null || searchText == null) {
            return new PageImpl<>(new LinkedList<>(), pageable, 0);
        }
        String text = "%" + searchText + "%";
        if (user.getRole().equals("ROLE_STUDENT")) {
            return marksRepository.searchByDescriptionNameAndUser(pageable, text, user);
        }
        if (user.getRole().equals("ROLE_PROFESSOR") || user.getRole().equals("ROLE_ADMIN")) {
            return marksRepository.searchByDescriptionAndName(pageable, text);
        }
        return new PageImpl<>(new LinkedList<>(), pageable, 0);
    }

    public void setMarkResend(boolean revised, Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String dni = auth != null ? auth.getName() : null;
        Mark mark = marksRepository.findById(id).orElse(null);
        if (dni == null || mark == null || mark.getUser() == null || mark.getUser().getDni() == null) {
            return;
        }
        if (mark.getUser().getDni().equals(dni)) {
            marksRepository.updateResend(revised, id);
        }
    }
}
