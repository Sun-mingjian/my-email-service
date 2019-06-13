package au.com.michael.constant;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_GATEWAY;
import static org.springframework.http.HttpStatus.GATEWAY_TIMEOUT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public final class RequestHeaderConstants {

    public static final List<Integer> HTTP_CODE_TO_INDICATE_SERVER_ISSUE = Arrays.asList(
            INTERNAL_SERVER_ERROR.value(),
            BAD_GATEWAY.value(),
            GATEWAY_TIMEOUT.value());
}
