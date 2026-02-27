package com.uniovi.sdi.grademanager.validators;

import com.uniovi.sdi.grademanager.entities.Department;
import com.uniovi.sdi.grademanager.services.DepartmentsService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class DepartmentAddFormValidator implements Validator {

    private final DepartmentsService departmentsService;

    public DepartmentAddFormValidator(DepartmentsService departmentsService) {
        this.departmentsService = departmentsService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Department.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Department department = (Department) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "code", "Error.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "Error.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "faculty", "Error.empty");

        if (department.getCode() != null) {
            if (!department.getCode().equals(department.getCode().trim())) {
                errors.rejectValue("code", "Error.department.code.trim");
            }
            if (department.getCode().length() != 9) {
                errors.rejectValue("code", "Error.department.code.length");
            } else {
                char lastChar = department.getCode().charAt(department.getCode().length() - 1);
                if (!Character.isLetter(lastChar)) {
                    errors.rejectValue("code", "Error.department.code.lastletter");
                }
            }
            if (departmentsService.existsByCode(department.getCode())) {
                errors.rejectValue("code", "Error.department.code.duplicate");
            }
        }

        if (department.getName() != null && !department.getName().equals(department.getName().trim())) {
            errors.rejectValue("name", "Error.department.name.trim");
        }

        if (department.getFaculty() != null && !department.getFaculty().equals(department.getFaculty().trim())) {
            errors.rejectValue("faculty", "Error.department.faculty.trim");
        }

        if (department.getProfessors() <= 0) {
            errors.rejectValue("professors", "Error.department.professors.positive");
        }
    }
}
