package au.com.michael.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = EmailAddressDuplicationValidator.class)
@Target({TYPE})
@Retention(RUNTIME)
public @interface EmailAddressDuplicationValidation {
    /**
     * Added from @Constraint. Holds the message code which will be mapped to the
     * message.properties file
     *
     * @return
     */
    String message() default "Duplicate emails";

    /**
     * Added from @Constraint. Definition from @Constraint - for user to customize
     * the targeted groups
     *
     * @return
     */
    Class<?>[] groups() default {};

    /**
     * Added from @Constraint. Definition from @Constraint - for extensibility
     * purposes
     *
     * @return
     */
    Class<? extends Payload>[] payload() default {};
}
