package com.pedrooliveira.rangolist.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class HandleIDNotFound extends RuntimeException {
  public HandleIDNotFound(String message) {
    super(message);
  }
}
