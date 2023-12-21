package com.venkat.airlinewebapp.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;


@Documented
@Retention(RUNTIME)
@Target(FIELD)
@Constraint(validatedBy = FlightMfdByValidator.class)
public @interface FlightMfdBy {

	public String message() default "The manufacturer value is invalid";
	public Class<?>[] groups() default {};
	public Class<? extends Payload>[] payload() default {};
}
