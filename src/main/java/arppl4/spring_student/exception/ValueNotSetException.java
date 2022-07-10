package arppl4.spring_student.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValueNotSetException extends RuntimeException {
    public ValueNotSetException(String message) {
        super(message);
    }
}
