package ua.exception;

@SuppressWarnings({"unused", "serial"})
public class FailedUpdateModelException extends RuntimeException {

    public FailedUpdateModelException(){
        super();
    }
    public FailedUpdateModelException(String message){
        super(message);
    }
}
