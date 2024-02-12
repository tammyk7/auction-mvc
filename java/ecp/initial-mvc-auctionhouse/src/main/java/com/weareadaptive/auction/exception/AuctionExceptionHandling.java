package com.weareadaptive.auction.exception;

import com.weareadaptive.auction.model.BusinessException;
import com.weareadaptive.auction.model.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class AuctionExceptionHandling extends ResponseEntityExceptionHandler
{
    @ExceptionHandler(value = {BusinessException.class, NullPointerException.class})
    protected ResponseEntity<ExceptionHandlingResponse<Object>> handleBadRequest(
            final RuntimeException exception)
    {
        final ExceptionHandlingResponse<Object> exceptionHandlingResponse = new ExceptionHandlingResponse<>(
                exception.getMessage());
        return new ResponseEntity<>(exceptionHandlingResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<ExceptionHandlingResponse<Object>> handleNotFound(
            final RuntimeException exception)
    {
        final ExceptionHandlingResponse<Object> exceptionHandlingResponse = new ExceptionHandlingResponse<>(
                exception.getMessage());
        return new ResponseEntity<>(exceptionHandlingResponse, HttpStatus.NOT_FOUND);
    }
}
