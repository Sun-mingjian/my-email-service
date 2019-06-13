package au.com.michael.controller;


import com.sendgrid.Response;
import au.com.michael.dto.EmailDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import au.com.michael.service.EmailService;

import javax.validation.Valid;

import static au.com.michael.constant.RequestHeaderConstants.HTTP_CODE_TO_INDICATE_SERVER_ISSUE;

@RestController
@RequestMapping("/email")
public class EmailController {
    private static final Logger LOG = LoggerFactory.getLogger(EmailController.class);

    private static final String MAIL_GUN_NAME = "mailgun";
    private static final String SEND_GRID_NAME = "sendgrid";


    @Autowired
    private final EmailService emailService;


    /**
     * Constructor with autowired parameters
     */
    public EmailController(final EmailService emailService) {
        this.emailService = emailService;
    }


    /**
     * Performs sending email operation
     */

    @PostMapping
    public final Response sendEmail(
            @Valid @RequestBody final EmailDto emailDto) throws Exception {
        LOG.info("Performing sending Email");

        Response response = emailService.sendEmailFromSendGrid(emailDto);

        if (HTTP_CODE_TO_INDICATE_SERVER_ISSUE.contains(response.getStatusCode())) {
            response = emailService.sendEmailFromMailGun(emailDto);
        }

        return response;
    }

    @PostMapping("/{providerName}")
    public final Response sendEmail(
            @Valid @RequestBody final EmailDto emailDto,
            @PathVariable("providerName") final String providerName) throws Exception {
        LOG.info("Performing sending Email by " + providerName);

        Response response;
        if (providerName.equals(MAIL_GUN_NAME)) {
            response = emailService.sendEmailFromMailGun(emailDto);
        } else {
            response = emailService.sendEmailFromSendGrid(emailDto);
        }
        return response;
    }
}
