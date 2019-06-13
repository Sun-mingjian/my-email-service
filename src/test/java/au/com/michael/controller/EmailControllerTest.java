package au.com.michael.controller;

import au.com.michael.dto.EmailDto;
import au.com.michael.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sendgrid.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(EmailController.class)
public class EmailControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private EmailService emailService;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Test
    public void ShouldReturnStatus422WhenContentIsMissing()
            throws Exception {
        final EmailDto emailDto = EmailDto.builder()
                .fromSender("a@ab.com")
                .subject("hei")
                .recipients(asList("a@a.com", "b@b.com"))
                .build();

        mvc.perform(post("/email")
                .contentType(APPLICATION_JSON_VALUE).content(OBJECT_MAPPER.writeValueAsString(emailDto)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$[0].reason").value("content field is mandatory"))
                .andExpect(jsonPath("$[0].reasonCode").value("missing_information"));

        verifyZeroInteractions(emailService);
    }

    @Test
    public void ShouldReturnStatus422WhenSubjectIsMissing()
            throws Exception {
        final EmailDto emailDto = EmailDto.builder()
                .fromSender("aaa@ab.com")
                .content("lala")
                .recipients(asList("a@a.com", "b@b.com"))
                .build();

        mvc.perform(post("/email")
                .contentType(APPLICATION_JSON_VALUE).content(OBJECT_MAPPER.writeValueAsString(emailDto)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$[0].reason").value("subject field is mandatory"))
                .andExpect(jsonPath("$[0].reasonCode").value("missing_information"));

        verifyZeroInteractions(emailService);
    }

    @Test
    public void ShouldReturnStatus422WhenFromIsMissing()
            throws Exception {
        final EmailDto emailDto = EmailDto.builder()
                .subject("hello")
                .content("lala")
                .recipients(asList("a@a.com", "b@b.com"))
                .build();

        mvc.perform(post("/email")
                .contentType(APPLICATION_JSON_VALUE).content(OBJECT_MAPPER.writeValueAsString(emailDto)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$[0].reason").value("from field is mandatory"))
                .andExpect(jsonPath("$[0].reasonCode").value("missing_information"));

    }

    @Test
    public void ShouldReturnStatus422WhenToIsMissing()
            throws Exception {
        final EmailDto emailDto = EmailDto.builder()
                .subject("hello")
                .fromSender("aaa@ab.com")
                .content("lala")
                .build();

        mvc.perform(post("/email")
                .contentType(APPLICATION_JSON_VALUE).content(OBJECT_MAPPER.writeValueAsString(emailDto)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$[0].reason").value("To field is mandatory, and there should be At least one recipient"))
                .andExpect(jsonPath("$[0].reasonCode").value("missing_information"));

    }

    @Test
    public void ShouldReturnStatus422WhenThereAreDuplicateEmails()
            throws Exception {
        final EmailDto emailDto = EmailDto.builder()
                .content("haha")
                .fromSender("a@ab.com")
                .subject("hei")
                .recipients(asList("a@a.com", "b@b.com"))
                .bccRecipients(asList("a@a.com", "b@b.com"))
                .ccRecipients(asList("a@a.com", "b@b.com"))
                .build();

        mvc.perform(post("/email")
                .contentType(APPLICATION_JSON_VALUE).content(OBJECT_MAPPER.writeValueAsString(emailDto)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$[0].reason").value("Each email address between to, cc, and bcc field should be unique"))
                .andExpect(jsonPath("$[0].reasonCode").value("email_address_duplicate"));

        verifyZeroInteractions(emailService);
    }

    @Test
    public void ShouldReturnStatus200WhenEverythingIsFine()
            throws Exception {
        final EmailDto emailDto = EmailDto.builder()
                .content("haha")
                .fromSender("a@ab.com")
                .subject("hei")
                .recipients(asList("a@a.com", "b@b.com"))
                .build();
        Response response = new Response(200, null, null);
        when(emailService.sendEmailFromSendGrid(emailDto)).thenReturn(response);

        mvc.perform(post("/email")
                .contentType(APPLICATION_JSON_VALUE).content(OBJECT_MAPPER.writeValueAsString(emailDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void ShouldSwitchToAnotherServiceProviderIfOneIsDown()
            throws Exception {
        final EmailDto emailDto = EmailDto.builder()
                .content("haha")
                .fromSender("a@ab.com")
                .subject("hei")
                .recipients(asList("a@a.com", "b@b.com"))
                .build();
        Response response = new Response(500, null, null);
        when(emailService.sendEmailFromSendGrid(emailDto)).thenReturn(response);
        Response response2 = new Response(200, null, null);
        when(emailService.sendEmailFromMailGun(emailDto)).thenReturn(response2);

        mvc.perform(post("/email")
                .contentType(APPLICATION_JSON_VALUE).content(OBJECT_MAPPER.writeValueAsString(emailDto)))
                .andExpect(status().isOk());

    }

}