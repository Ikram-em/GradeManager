package com.uniovi.sdi.grademanager.controllers;

import com.uniovi.sdi.grademanager.entities.User;
import com.uniovi.sdi.grademanager.services.SecurityService;
import com.uniovi.sdi.grademanager.services.UsersService;
import com.uniovi.sdi.grademanager.validators.UserEditFormValidator;
import com.uniovi.sdi.grademanager.validators.SignUpFormValidator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;

@Controller
public class UsersController {

    private final UsersService usersService;
    private final SecurityService securityService;
    private final SignUpFormValidator signUpFormValidator;
    private final UserEditFormValidator userEditFormValidator;

    public UsersController(
            UsersService usersService,
            SecurityService securityService,
            SignUpFormValidator signUpFormValidator,
            UserEditFormValidator userEditFormValidator) {
        this.usersService = usersService;
        this.securityService = securityService;
        this.signUpFormValidator = signUpFormValidator;
        this.userEditFormValidator = userEditFormValidator;
    }

    @GetMapping("/user/list")
    public String getListado(Model model) {
        model.addAttribute("usersList", usersService.getUsers());
        return "user/list";
    }

    @GetMapping("/user/list/update")
    public String updateList(Model model) {
        model.addAttribute("usersList", usersService.getUsers());
        return "user/fragments/usersTable :: usersTable";
    }

    @GetMapping("/user/add")
    public String getUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("usersList", usersService.getUsers());
        return "user/add";
    }

    @GetMapping("/signup")
    public String getSignup(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/user/add")
    public String setUser(@ModelAttribute User user) {
        usersService.addUser(user);
        return "redirect:/user/list";
    }

    @PostMapping("/signup")
    public String signup(@Validated @ModelAttribute("user") User user, BindingResult result) {
        signUpFormValidator.validate(user, result);
        if (result.hasErrors()) {
            return "signup";
        }
        usersService.addUser(user);
        securityService.autoLogin(user.getDni(), user.getPasswordConfirm());
        return "redirect:home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/home")
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String dni = auth != null ? auth.getName() : null;
        if (dni == null || "anonymousUser".equals(dni)) {
            model.addAttribute("markList", Collections.emptyList());
            model.addAttribute("marksList", Collections.emptyList());
        } else {
            User activeUser = usersService.getUserByDni(dni);
            if (activeUser == null || activeUser.getMarks() == null) {
                model.addAttribute("markList", Collections.emptyList());
                model.addAttribute("marksList", Collections.emptyList());
            } else {
                model.addAttribute("markList", activeUser.getMarks());
                model.addAttribute("marksList", activeUser.getMarks());
            }
        }
        return "home";
    }

    @GetMapping("/user/details/{id}")
    public String getDetail(Model model, @PathVariable Long id) {
        model.addAttribute("user", usersService.getUser(id));
        return "user/details";
    }

    @GetMapping("/user/delete/{id}")
    public String delete(@PathVariable Long id) {
        usersService.deleteUser(id);
        return "redirect:/user/list";
    }

    @GetMapping("/user/edit/{id}")
    public String getEdit(Model model, @PathVariable Long id) {
        User user = usersService.getUser(id);
        if (user == null) {
            return "redirect:/user/list";
        }
        model.addAttribute("user", user);
        return "user/edit";
    }

    @PostMapping("/user/edit/{id}")
    public String setEdit(@PathVariable Long id, @Validated @ModelAttribute("user") User user, BindingResult result) {
        User currentUser = usersService.getUser(id);
        if (currentUser == null) {
            return "redirect:/user/list";
        }

        user.setId(id);
        userEditFormValidator.validate(user, result);
        if (result.hasErrors()) {
            return "user/edit";
        }

        usersService.updateUserProfile(id, user.getDni(), user.getName(), user.getLastName());
        return "redirect:/user/details/" + id;
    }
}
