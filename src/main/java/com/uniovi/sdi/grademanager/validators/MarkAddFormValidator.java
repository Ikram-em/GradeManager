package com.uniovi.sdi.grademanager.validators;

import com.uniovi.sdi.grademanager.entities.Mark;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class MarkAddFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Mark.class.equals(clazz);
    }


    //Este método valida q la nota sea entre 0 y 10
    //una descripcion de al menso 5 carácteres
    @Override
    public void validate(Object target, Errors errors) {
        Mark mark = (Mark) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "Error.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "score", "Error.empty");

        if (mark.getDescription() != null) {
            if (!mark.getDescription().equals(mark.getDescription().trim())) {
                errors.rejectValue("description", "Error.mark.description.trim");
            }
            if (mark.getDescription().trim().length() < 20) {
                errors.rejectValue("description", "Error.mark.description.length");
            }
        }

        if (mark.getScore() != null && (mark.getScore() < 0 || mark.getScore() > 10)) {
            errors.rejectValue("score", "Error.mark.score.range");
        }
    }
}
