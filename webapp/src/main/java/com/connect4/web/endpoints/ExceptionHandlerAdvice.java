package com.connect4.web.endpoints;

import com.connect4.domain.exceptions.GameException;
import com.connect4.web.datastore.ResourceNotFoundException;
import com.google.common.collect.ImmutableMap;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;
import java.util.Map;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler({ResourceNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    public Map entityNotFoundExceptionHandler(ResourceNotFoundException e) throws IOException {
        return errorMessage("Resource not found, id: " + e.getId());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map validationExceptionHandler(MethodArgumentNotValidException e) throws IOException {
        FieldError fieldError = e.getBindingResult().getFieldError();
        return errorMessage("Validation failed: " + fieldError.getField() + " " + fieldError.getDefaultMessage());
    }

    @ExceptionHandler({GameException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map gameExceptionHandler(GameException e) throws IOException {
        return errorMessage(e.getMessage());
    }

    private ImmutableMap<String, Object> errorMessage(String message) {
        return ImmutableMap.of("message", message);
    }
}
