package ua.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
@SuppressWarnings("unused")
public class ModelNotFoundException extends RuntimeException {
    public ModelNotFoundException(String msg) {
        super(msg);
    }

    public ModelNotFoundException(String msg, Exception e) {
        super(msg, e);
    }
}
