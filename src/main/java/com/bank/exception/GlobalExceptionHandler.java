
package com.bank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.bank.dto.ResponseStructure;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseStructure<String> handleRecordNotFound(
            RecordNotFoundException ex) {

        ResponseStructure<String> res =
                new ResponseStructure<>();

        res.setStatusCode(HttpStatus.NOT_FOUND.value());
        res.setMessage(ex.getMessage());
        res.setData(null);

        return res;
    }

    @ExceptionHandler(IdNotFoundException.class)
    public ResponseStructure<String> handleIdNotFound(
            IdNotFoundException ex) {

        ResponseStructure<String> res =
                new ResponseStructure<>();

        res.setStatusCode(HttpStatus.NOT_FOUND.value());
        res.setMessage(ex.getMessage());
        res.setData(null);

        return res;
    }
}
