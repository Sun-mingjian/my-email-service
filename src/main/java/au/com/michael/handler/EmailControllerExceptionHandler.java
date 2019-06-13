package au.com.michael.handler;

import au.com.michael.dto.ErrorMessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

import static java.util.Locale.US;
import static org.springframework.http.ResponseEntity.unprocessableEntity;

@ControllerAdvice
public final class EmailControllerExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(EmailControllerExceptionHandler.class);

    private static final String INVALID_EMAIL_ADDRESS_ERROR_MSG = "Does not contain a valid address";
    private static final String INVALID_EMAIL_RESPONSE_MSG = "Invalid.emailAddress";
    private static final String INVALID_EMAIL_RESPONSE_MSG_MG = "parameter is not a valid address";
    private static final String INVALID_DOMAIN_RESPONSE_MSG_MG = "Please add your own domain or add the address to authorized recipients in Account Settings";

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<List<ErrorMessageDto>> handleMethodArgumentNotValidException(
            final MethodArgumentNotValidException methodArgumentNotValidException) {
        final List<ErrorMessageDto> errorMessages = new ArrayList();
        final List<ObjectError> allErrors = methodArgumentNotValidException.getBindingResult().getAllErrors();
        allErrors.stream().forEach(e -> errorMessages.add(ErrorMessageDto.builder()
                .reason(messageSource.getMessage(e.getDefaultMessage(), null, US))
                .reasonCode(messageSource.getMessage(e.getCode(), null, US))
                .build()));
        return unprocessableEntity().body(errorMessages);
    }

    @ExceptionHandler(InvalidSendGridRequestException.class)
    protected ResponseEntity<List<ErrorMessageDto>> handleInvalidSendGridRequestException(
            final InvalidSendGridRequestException exception) {
        final List<ErrorMessageDto> errorMessages = new ArrayList();
        LOG.info(exception.getMessage());
        if (exception.getMessage().contains(INVALID_EMAIL_ADDRESS_ERROR_MSG)) {
            errorMessages.add(ErrorMessageDto.builder()
                    .reason(messageSource.getMessage(INVALID_EMAIL_RESPONSE_MSG, null, US))
                    .reasonCode(messageSource.getMessage("Invalid_Email", null, US))
                    .build());
        }
        return unprocessableEntity().body(errorMessages);
    }

    @ExceptionHandler(InvalidMailGunDomainException.class)
    protected ResponseEntity<List<ErrorMessageDto>> handleMailGunInvalidDomainException(
            final InvalidMailGunDomainException exception) {
        final List<ErrorMessageDto> errorMessages = new ArrayList();
        LOG.info(exception.getMessage());
        errorMessages.add(ErrorMessageDto.builder()
                .reason(messageSource.getMessage(exception.getMessage(), null, US))
                .reasonCode(messageSource.getMessage("Invalid_Email", null, US))
                .build());

        return unprocessableEntity().body(errorMessages);
    }

    @ExceptionHandler(InvalidMailGunRequestException.class)
    protected ResponseEntity<List<ErrorMessageDto>> handleInvalidMailGunRequestException(
            final InvalidMailGunRequestException exception) {
        final List<ErrorMessageDto> errorMessages = new ArrayList();
        LOG.info(exception.getMessage());
        if (exception.getMessage().contains(INVALID_EMAIL_RESPONSE_MSG_MG)) {
            errorMessages.add(ErrorMessageDto.builder()
                    .reason(exception.getMessage().replace("{\"message\":\"", "")
                            .replace(". please check documentation\"}", ""))
                    .reasonCode(messageSource.getMessage("Invalid_Email", null, US))
                    .build());
        } else if (exception.getMessage().contains(INVALID_DOMAIN_RESPONSE_MSG_MG)) {
            errorMessages.add(ErrorMessageDto.builder()
                    .reason(messageSource.getMessage("Invalid.Domain", null, US))
                    .reasonCode(messageSource.getMessage("Invalid_Email", null, US))
                    .build());
        }
        return unprocessableEntity().body(errorMessages);
    }
}
