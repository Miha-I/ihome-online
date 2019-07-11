package ua.exception;

@SuppressWarnings({"unused", "serial"})
public class FailedRemoveModelException extends RuntimeException {

    public FailedRemoveModelException(){
        super();
    }
    public FailedRemoveModelException(String message){
        super(message);
    }
}
