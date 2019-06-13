package au.com.michael.validator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class RecipientsValidatorTest {

    private final RecipientsValidator underTest = new RecipientsValidator();

    @Test
    public void shouldReturnFalseIfThereIsNoRecipients() {
        assertThat(
                underTest.isValid(new ArrayList<>(), null))
                .isFalse();
    }

    @Test
    public void shouldReturnTrueIfThereAreRecipients() {
        assertThat(
                underTest.isValid(asList("a@a.com,b@b.com"), null))
                .isTrue();
    }
}