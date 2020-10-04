package pl.kozhanov.taskmanager.service.validation;

import org.springframework.beans.factory.annotation.Autowired;
import pl.kozhanov.taskmanager.service.UserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NewUserValidator implements ConstraintValidator<NewUserConstraint, String> {

    @Autowired
    UserService userService;

    @Override
    public void initialize(NewUserConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        if (userService.findAll().contains(userService.findByUsername(username))) return false;
        else return true;
    }
}
