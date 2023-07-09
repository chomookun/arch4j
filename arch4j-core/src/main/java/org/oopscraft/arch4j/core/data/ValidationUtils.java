package org.oopscraft.arch4j.core.data;

import javax.validation.*;
import java.util.Set;

public class ValidationUtils {

    public static <T> void validate(T target) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(target);
        if(!constraintViolations.isEmpty()){
            throw new ConstraintViolationException(constraintViolations);
        }
    }

}
