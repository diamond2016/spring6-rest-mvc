package guru.springframework.spring6restmvc.controller;

import org.springframework.http.ResponseEntity;

// @ControllerAdvice
public class ExceptionController {
    // @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Void> handleNotFoundException() {
        System.out.println("In exception controller (advice)...");
        return ResponseEntity.notFound().build();
    }
}
