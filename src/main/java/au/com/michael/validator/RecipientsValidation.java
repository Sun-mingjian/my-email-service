package au.com.michael.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

@Documented
@Constraint(validatedBy = RecipientsValidator.class)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface RecipientsValidation {
    /**
     * Added from @Constraint. Holds the message code which will be mapped to the
     * message.properties file
     *
     * @return
     */
    String message() default "Invalid agents format";

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
