package au.com.michael.handler;

public class InvalidSendGridRequestException extends RuntimeException {

    public InvalidSendGridRequestException(final String message) {
        super(message);
    }

}
