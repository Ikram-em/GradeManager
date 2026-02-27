package com.uniovi.sdi.grademanager.validators;

import com.uniovi.sdi.grademanager.entities.User;
import com.uniovi.sdi.grademanager.services.UsersService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserEditFormValidator implements Validator {

    private final UsersService usersService;

    public UserEditFormValidator(UsersService usersService) {
        this.usersService = usersService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dni", "Error.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "Error.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "Error.empty");

        if (user.getDni() != null && (user.getDni().length() < 5 || user.getDni().length() > 24)) {
            errors.rejectValue("dni", "Error.signup.dni.length");
        }
        if (user.getName() != null && (user.getName().length() < 5 || user.getName().length() > 24)) {
            errors.rejectValue("name", "Error.signup.name.length");
        }
        if (user.getLastName() != null && (user.getLastName().length() < 5 || user.getLastName().length() > 24)) {
            errors.rejectValue("lastName", "Error.signup.lastName.length");
        }

        User existingUser = usersService.getUserByDni(user.getDni());
        if (existingUser != null && !existingUser.getId().equals(user.getId())) {
            errors.rejectValue("dni", "Error.signup.dni.duplicate");
        }
    }
}
