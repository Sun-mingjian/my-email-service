package au.com.michael;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class EmailSenderRestAPI {
    public static void main(final String[] args) {
        SpringApplication.run(EmailSenderRestAPI.class, args);
    }
}

