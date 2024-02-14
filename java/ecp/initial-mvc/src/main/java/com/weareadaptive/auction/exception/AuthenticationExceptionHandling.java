package com.weareadaptive.auction.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@ControllerAdvice
public class AuthenticationExceptionHandling implements AuthenticationEntryPoint
{
    @Override
    public void commence(final HttpServletRequest request, final HttpServletResponse response,
                         final AuthenticationException authException) throws IOException, ServletException
    {
        // 401
        System.out.println("MESSAGE " + authException.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication Failed");
    }

    @ExceptionHandler(value = {Exception.class})
    public void commence(final HttpServletRequest request, final HttpServletResponse response,
                         final Exception exception) throws IOException
    {
        // 500
        System.out.println("MESSAGE " + exception.getMessage());
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                "Internal Server Error : " + exception.getMessage());
    }

    public static class BusinessException extends RuntimeException
    {
        public BusinessException(final String message)
        {
            super(message);
        }
    }
}
