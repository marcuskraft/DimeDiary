package com.dimediary.rest.controller.helper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ResponseFactory {

  public <T> ResponseEntity<T> created(
      final T body) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(body);
  }

  public ResponseEntity<Void> okNoContent() {
    return ResponseEntity.noContent().build();
  }

  public <T> ResponseEntity<T> ok(final T body) {
    return ResponseEntity.ok(body);
  }

  public ResponseEntity<Void> badRequest() {
    return ResponseEntity.badRequest().build();
  }

}
