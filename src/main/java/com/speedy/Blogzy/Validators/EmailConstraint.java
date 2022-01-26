package com.speedy.Blogzy.Validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailRegisteredValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailConstraint {
    String message() default "Email already registered!!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
