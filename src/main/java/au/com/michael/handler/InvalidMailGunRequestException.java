package au.com.michael.handler;

public class InvalidMailGunRequestException extends RuntimeException {

    public InvalidMailGunRequestException(final String message) {
        super(message);
    }

}
