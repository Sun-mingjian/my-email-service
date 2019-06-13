package au.com.michael.service;

import com.sendgrid.Response;
import au.com.michael.dto.EmailDto;

import java.io.IOException;

public interface EmailService {

    /**
     * Send emails
     * @param emailDto
     */
    Response sendEmailFromSendGrid(EmailDto emailDto) throws IOException;

    Response sendEmailFromMailGun(EmailDto emailDto);

}
