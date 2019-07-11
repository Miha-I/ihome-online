package ua.handler;

import org.hibernate.TransactionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;

@ControllerAdvice
public class ControllerAdvisor {

    private Logger logger = LoggerFactory.getLogger(ControllerAdvisor.class);

    @ResponseBody
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public String handleHttpMediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException ex) {
        logger.error("HttpMediaTypeNotAcceptableException - " + ex.getMessage());
        return "acceptable MIME type:" + MediaType.APPLICATION_JSON_VALUE;
    }

    @ExceptionHandler(SQLException.class)
    public String handleSQLException(Model model, HttpServletRequest request, SQLException ex){
        logger.error("SQLException occurred:: URL - " + request.getRequestURL() + " SQLException - " + ex.getMessage());
        model.addAttribute("message", ex.getMessage());
        return "forward:/error/exception";
    }

    @ResponseStatus(value= HttpStatus.NOT_FOUND, reason="IOException occurred")
    @ExceptionHandler(IOException.class)
    public void handleIOException(IOException ex){
        logger.error("IOException " + ex.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleINotFound(Model model, NoHandlerFoundException ex){
        logger.error("NoHandlerFoundException - " + ex.getMessage());
        model.addAttribute("message", ex.getMessage());
        return "forward:/error/pageNotFound";
    }

    @ExceptionHandler(TransactionException.class)
    public String handleITransactionException(Model model, Exception ex){
        logger.error("TransactionException - " + ex.getMessage());
        model.addAttribute("message", ex.getMessage());
        return "forward:/error/exception";
    }

    @ExceptionHandler(Exception.class)
    public String handleIAllException(Model model, Exception ex){
        logger.error("Exception - " + ex.getMessage());
        model.addAttribute("message", ex.getMessage());
        return "forward:/error/exception";
    }
}
