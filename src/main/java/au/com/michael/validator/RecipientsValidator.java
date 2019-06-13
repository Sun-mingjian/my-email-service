package au.com.michael.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class RecipientsValidator implements ConstraintValidator<RecipientsValidation, List<String>> {

    @Override
    public final boolean isValid(final List<String> Recipients, final ConstraintValidatorContext context) {

        return Recipients.isEmpty() ? false : true;
    }
}
