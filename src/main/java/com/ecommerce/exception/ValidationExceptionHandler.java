package com.ecommerce.exception;

import com.ecommerce.dto.ValidationExceptionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ValidationExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<ValidationExceptionDTO> handle(MethodArgumentNotValidException exception) {

        List<ValidationExceptionDTO> errorList = new ArrayList<>();
        List<FieldError> fieldErrorList = exception.getBindingResult().getFieldErrors();

        fieldErrorList.forEach(e -> {

            String message = messageSource.getMessage(e, LocaleContextHolder.getLocale());
            ValidationExceptionDTO error = new ValidationExceptionDTO(e.getField(), message);
            errorList.add(error);
        });

        return errorList;
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public List<ValidationExceptionDTO> handle(HttpMessageNotReadableException exception) {

        List<ValidationExceptionDTO> errorList = new ArrayList<>();
        String message = exception.getMessage();

        if (message == null) {
            message = exception.getCause().toString();
        }

        ValidationExceptionDTO error = new ValidationExceptionDTO("", message);
        errorList.add(error);

        return errorList;
    }

    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    public List<ValidationExceptionDTO> handle(AuthenticationException exception) {

        List<ValidationExceptionDTO> errorList = new ArrayList<>();
        String message = exception.getMessage();

        if (message == null) {
            message = exception.getCause().toString();
        }

        ValidationExceptionDTO error = new ValidationExceptionDTO("", message);
        errorList.add(error);

        return errorList;
    }
}
