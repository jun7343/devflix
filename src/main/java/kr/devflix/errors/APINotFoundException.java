package kr.devflix.errors;

public class APINotFoundException extends RuntimeException {

    public APINotFoundException(String message) {
        super(message);
    }

    public APINotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
