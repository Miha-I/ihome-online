package ua.exception;

@SuppressWarnings("unused")
public class RemoveModelException extends RuntimeException {
    public RemoveModelException(String msg) {
        super(msg);
    }

    public RemoveModelException(String msg, Exception e) {
        super(msg, e);
    }
}
