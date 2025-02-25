package org.chomookun.arch4j.core.common.data;

import jakarta.validation.*;
import java.util.Set;

public class ValidationUtil {

    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

    private static final Validator validator = factory.getValidator();

    private ValidationUtil() {}

    public static <T> void validate(T target) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(target);
        if(!constraintViolations.isEmpty()){
            throw new ConstraintViolationException(constraintViolations);
        }
    }

}
