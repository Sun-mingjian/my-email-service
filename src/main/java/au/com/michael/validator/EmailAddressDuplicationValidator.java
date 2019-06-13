package au.com.michael.validator;

import au.com.michael.dto.EmailDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EmailAddressDuplicationValidator implements ConstraintValidator<EmailAddressDuplicationValidation,
        EmailDto> {

    @Override
    public final boolean isValid(final EmailDto emailDto, final ConstraintValidatorContext context) {

        List<String> allMailList = new ArrayList<>();
        allMailList.addAll(emailDto.getRecipients());
        allMailList.addAll(emailDto.getCcRecipients());
        allMailList.addAll(emailDto.getBccRecipients());

        if(allMailList.stream().filter(i -> Collections.frequency(allMailList, i) >1).findFirst().isPresent()){
            return false;
        }
        return true;
    }
}
