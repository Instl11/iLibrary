package lib.iLibrary.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RESTBookNotFoundException extends RuntimeException {

    public RESTBookNotFoundException() {
    }

    public RESTBookNotFoundException(String message) {
        super(message);
    }
}
