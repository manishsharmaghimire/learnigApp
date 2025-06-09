package com.elearn.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PriceAndDiscountValidator.class)
@Documented
public @interface ValidPriceAndDiscount {
    String message() default "Discount cannot be greater than price";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
