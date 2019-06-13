package au.com.michael.service;

import au.com.michael.handler.InvalidMailGunRequestException;
import au.com.michael.handler.InvalidSendGridRequestException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import au.com.michael.dto.EmailDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static org.apache.tomcat.util.buf.StringUtils.join;

@Service
public class EmailServiceImpl implements EmailService {

    private final static String TO_TYPE = "to_type";
    private final static String CC_TYPE = "cc_type";
    private final static String BCC_TYPE = "bcc_type";

    @Value("${SendGrid.apikey}")
    private String sendGridApikey;

    @Value("${MailGun.apikey}")
    private String mailGunApikey;

    @Value("${MailGun.TestDomainName}")
    private String mailGunTestDomainName;


    @Override
    public Response sendEmailFromSendGrid(final EmailDto emailDto) {
        final Email from = new Email(emailDto.getFromSender(), emailDto.getFromSender());
        final Email to = new Email(emailDto.getRecipients().get(0), emailDto.getRecipients().get(0));
        final String subject = emailDto.getSubject();

        final Content content = new Content("text/plain", emailDto.getContent());
        final Mail mail = new Mail(from, subject, to, content);
        addOtherRecipient(mail, emailDto);

        SendGrid sg = new SendGrid(sendGridApikey);

        try {
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            return sg.api(request);
        } catch (IOException e) {
            throw new InvalidSendGridRequestException(e.getMessage());
        }
    }

    @Override
    public Response sendEmailFromMailGun(EmailDto emailDto) {

        try {
            HttpResponse<JsonNode> request = Unirest.post("https://api.mailgun.net/v3/" + mailGunTestDomainName + "/messages")
                    .basicAuth("api", mailGunApikey)
                    .queryString("from", emailDto.getFromSender())
                    .queryString("to", join(emailDto.getRecipients(), ';'))
                    .queryString("cc", join(emailDto.getCcRecipients(), ';'))
                    .queryString("bcc", join(emailDto.getBccRecipients(), ';'))
                    .queryString("subject", emailDto.getSubject())
                    .queryString("text", emailDto.getContent())
                    .asJson();
            if (request.getStatus() == HttpStatus.BAD_REQUEST.value()) {
                throw new InvalidMailGunRequestException(request.getBody().toString());
            }
            return new Response(request.getStatus(), request.getBody().toString(), null);
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void addOtherRecipient(final Mail mail, final EmailDto emailDto) {
        final Personalization newPersonalization = new Personalization();
        addCertainRecipientsToPersonalization(emailDto.getRecipients(), newPersonalization, TO_TYPE);
        addCertainRecipientsToPersonalization(emailDto.getBccRecipients(), newPersonalization, BCC_TYPE);
        addCertainRecipientsToPersonalization(emailDto.getCcRecipients(), newPersonalization, CC_TYPE);
        mail.getPersonalization().clear();
        mail.getPersonalization().add(newPersonalization);
    }

    private void addCertainRecipientsToPersonalization(final List<String> recipients,
                                                       final Personalization newPersonalization, final String type) {
        if (recipients.isEmpty()) {
            return;
        }
        if (type.equals(TO_TYPE)) {
            recipients.stream().map(i -> new Email(i, i)).forEach(t -> newPersonalization.addTo(t));
        } else if (type.equals(BCC_TYPE)) {
            recipients.stream().map(i -> new Email(i, i)).forEach(t -> newPersonalization.addBcc(t));
        } else if (type.equals(CC_TYPE)) {
            recipients.stream().map(i -> new Email(i, i)).forEach(t -> newPersonalization.addCc(t));
        }
    }

}
