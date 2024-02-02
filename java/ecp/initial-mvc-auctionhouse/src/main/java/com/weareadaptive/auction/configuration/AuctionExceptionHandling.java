package com.weareadaptive.auction.configuration;

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
    protected ResponseEntity<Response<Object>> handleBadRequest(
            final RuntimeException exception)
    {
        final Response<Object> response = new Response<>(exception.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<Response<Object>> handleNotFound(
            final RuntimeException exception)
    {
        final Response<Object> response = new Response<>(exception.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
