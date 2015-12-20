package com.welflex.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target( value = {ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = HourMinuteValidator.class)
@Documented
public @interface HourMinute {
  String message() default "Invalid Hour:Minute Format";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
