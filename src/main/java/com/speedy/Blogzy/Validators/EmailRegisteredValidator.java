package com.speedy.Blogzy.Validators;

import com.speedy.Blogzy.repositories.AuthorRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailRegisteredValidator implements ConstraintValidator<EmailConstraint, String> {

    private final AuthorRepository authorRepository;

    public EmailRegisteredValidator(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public void initialize(EmailConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return !authorRepository.findByEmail(email).isPresent();
    }
}
