package au.com.michael.handler;

public class InvalidMailGunDomainException extends RuntimeException {

    public InvalidMailGunDomainException(final String message) {
        super(message);
    }

}