package ua.exception;

@SuppressWarnings({"unused", "serial"})
public class FailedSaveModelException extends RuntimeException {

    public FailedSaveModelException() {
        super();
    }
    public FailedSaveModelException(String message) {
        super(message);
    }
}
