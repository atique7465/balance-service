package com.atique.balanceservice.annotations;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author atiQue
 * @since 14'Jun 2023 at 11:13 PM
 */

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = AccountNoValidator.class)
@Documented
public @interface ValidAccountNo {

    String message() default "Account no is invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
