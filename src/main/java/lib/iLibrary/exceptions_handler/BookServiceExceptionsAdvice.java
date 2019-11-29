package lib.iLibrary.exceptions_handler;

import lib.iLibrary.exceptions.NoCurrentBookException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class BookServiceExceptionsAdvice {

    @ExceptionHandler({NoCurrentBookException.class})
    public String handleNoCurrentBookException() {
        log.error("NoCurrentBookException handler executed");
        return "noCurrentBook";
    }
}
