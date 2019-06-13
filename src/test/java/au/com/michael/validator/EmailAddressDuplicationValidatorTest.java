package au.com.michael.validator;

import au.com.michael.dto.EmailDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class EmailAddressDuplicationValidatorTest {

    private final EmailAddressDuplicationValidator underTest = new EmailAddressDuplicationValidator();

    @Test
    public void shouldReturnFalseIfDuplicateEmailsExist() {
        final EmailDto emailDto = EmailDto.builder()
                .recipients(asList("a@a.com", "b@b.com"))
                .bccRecipients(asList("a@a.com", "b@b.com"))
                .ccRecipients(asList("a@a.com", "b@b.com"))
                .build();
        assertThat(underTest.isValid(emailDto, null)).isFalse();
    }

    @Test
    public void shouldReturnTrueIfNoDuplicateEmails() {
        final EmailDto emailDto = EmailDto.builder()
                .recipients(asList("a@a.com", "b@b.com"))
                .bccRecipients(asList("a@a.com", "b@b.com"))
                .ccRecipients(asList("a@a.com", "b@b.com"))
                .build();
        assertThat(underTest.isValid(emailDto, null)).isFalse();
    }

    @Test
    public void shouldReturnFalseIfOnlyOneDuplicateEmailExists() {
        final EmailDto emailDto = EmailDto.builder()
                .recipients(asList("a@a.com", "a@a.com"))
                .build();
        assertThat(underTest.isValid(emailDto, null)).isFalse();
    }
}