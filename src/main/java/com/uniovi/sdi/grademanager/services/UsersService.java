package com.uniovi.sdi.grademanager.services;

import com.uniovi.sdi.grademanager.entities.User;
import com.uniovi.sdi.grademanager.repositories.UsersRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UsersService(UsersRepository usersRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.usersRepository = usersRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostConstruct
    public void init() {
    }

    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        usersRepository.findAll().forEach(users::add);
        return users;
    }

    public User getUser(Long id) {
        return usersRepository.findById(id).orElse(null);
    }

    public void addUser(User user) {
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        } else if (user.getId() != null) {
            User currentUser = usersRepository.findById(user.getId()).orElse(null);
            if (currentUser != null) {
                user.setPassword(currentUser.getPassword());
            }
        }
        usersRepository.save(user);
    }

    public User getUserByDni(String dni) {
        return usersRepository.findByDni(dni);
    }

    public void deleteUser(Long id) {
        usersRepository.deleteById(id);
    }
}
