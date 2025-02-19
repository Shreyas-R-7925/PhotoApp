package com.appsdeveloperblog.app.ws.mobile_app_ws.exceptions;

import com.appsdeveloperblog.app.ws.mobile_app_ws.io.enums.ErrorMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class AppExceptionsHandler {

    @ExceptionHandler(value = {UserServiceException.class})
    public ResponseEntity<Object> handleUserServiceException(UserServiceException ex, WebRequest request){

        ErrorMessage errorMessage = new ErrorMessage(new Date(), ex.getMessage());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

//    @ExceptionHandler(value = {Exception.class})
//    public ResponseEntity<Object> handleOtherExceptions(UserServiceException ex, WebRequest request){
//
//        ErrorMessage errorMessage = new ErrorMessage(new Date(), ex.getMessage());
//        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}
