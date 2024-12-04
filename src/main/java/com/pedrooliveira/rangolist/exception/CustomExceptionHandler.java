package com.pedrooliveira.rangolist.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

  @org.springframework.web.bind.annotation.ExceptionHandler(HandleIDNotFound.class)
  public ResponseEntity<Object> handleIdNotFound(HandleIDNotFound e) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("message", e.getMessage());

    return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
  }

  @org.springframework.web.bind.annotation.ExceptionHandler(HandleNoHasRestaurants.class)
  public ResponseEntity<Object> handleNoHasRestaurants(HandleNoHasRestaurants e) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("message", e.getMessage());

    return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
  }

  @org.springframework.web.bind.annotation.ExceptionHandler(HandleNoHasProducts.class)
  public ResponseEntity<Object> handleNoHasProducts(HandleNoHasProducts e) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("message", e.getMessage());

    return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
  }

  @org.springframework.web.bind.annotation.ExceptionHandler(HandleNoHasPromotions.class)
  public ResponseEntity<Object> handleNoHasPromotions(HandleNoHasPromotions e) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("message", e.getMessage());

    return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
  }

  @org.springframework.web.bind.annotation.ExceptionHandler(HandleValidationField.class)
  public ResponseEntity<Object> handleValidationField(HandleValidationField e) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("message", e.getMessage());

    return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
  }

  @org.springframework.web.bind.annotation.ExceptionHandler(HandleNoHasFile.class)
  public ResponseEntity<Object> handleNoHasFile(HandleNoHasFile e) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("message", e.getMessage());

    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }
}
