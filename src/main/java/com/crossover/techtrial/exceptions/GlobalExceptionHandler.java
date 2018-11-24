package com.crossover.techtrial.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.AbstractMap;

@ControllerAdvice
@Component
public class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Global Exception handler for all exceptions.
     */
    @ExceptionHandler
    public ResponseEntity<AbstractMap.SimpleEntry<String, String>> handle(Exception ex) {
        LOG.error("Exception: Unable to process this request. ", ex);
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = "Unable to process this request.";

        if (ex instanceof CustomException) {
            final CustomException exception = (CustomException) ex;
            httpStatus = exception.getCustomErrorCode().getHttpStatus();
        }
        // general exception
        AbstractMap.SimpleEntry<String, String> response = new AbstractMap.SimpleEntry<>("message", message);
        return ResponseEntity.status(httpStatus)
                .body(response);
    }
}
