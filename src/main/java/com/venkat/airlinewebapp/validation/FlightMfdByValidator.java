package com.venkat.airlinewebapp.validation;

import java.util.Arrays;
import java.util.List;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class FlightMfdByValidator implements ConstraintValidator<FlightMfdBy, String> {

	List<String> approvedMfd = Arrays.asList("Boeing", "Tata", "Airbus");

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null || value.isBlank())
			return false;
		if (approvedMfd.contains(value))
			return true;
		else
			return false;
	}

}
