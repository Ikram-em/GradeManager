package com.uniovi.sdi.grademanager.validators;

import com.uniovi.sdi.grademanager.entities.User;
import com.uniovi.sdi.grademanager.services.UsersService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class SignUpFormValidator implements Validator {

    private final UsersService usersService;

    public SignUpFormValidator(UsersService usersService) {
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
        if (user.getDni() != null && (user.getDni().length() < 5 || user.getDni().length() > 24)) {
            errors.rejectValue("dni", "Error.signup.dni.length");
        }
        if (user.getDni() != null && usersService.getUserByDni(user.getDni()) != null) {
            errors.rejectValue("dni", "Error.signup.dni.duplicate");
        }
        if (user.getName() != null && (user.getName().length() < 5 || user.getName().length() > 24)) {
            errors.rejectValue("name", "Error.signup.name.length");
        }
        if (user.getLastName() != null && (user.getLastName().length() < 5 || user.getLastName().length() > 24)) {
            errors.rejectValue("lastName", "Error.signup.lastName.length");
        }
        if (user.getPassword() != null && (user.getPassword().length() < 5 || user.getPassword().length() > 24)) {
            errors.rejectValue("password", "Error.signup.password.length");
        }
        if (user.getPasswordConfirm() != null && !user.getPasswordConfirm().equals(user.getPassword())) {
            errors.rejectValue("passwordConfirm", "Error.signup.passwordConfirm.coincidence");
        }
    }
}
