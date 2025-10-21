package guru.springframework.spring6restmvc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class CustomErrorController {

// catch all, default, no exception class in annotation
// ConstraintViolationException jakarta
@ExceptionHandler
    ResponseEntity<List<Map<String, String>>> handleJPAViolation(TransactionSystemException exception) {
        log.debug("handleJPAViolations:\n" + exception.getCause());
        ResponseEntity.BodyBuilder responseEntity = ResponseEntity.badRequest();

        if (exception.getCause().getCause() instanceof ConstraintViolationException) {
            ConstraintViolationException ve = (ConstraintViolationException) exception.getCause().getCause();
            List<Map<String, String>> errors = ve.getConstraintViolations().stream()
                            .map(constraintViolation -> {
                                Map<String, String> errMap = new HashMap<>();
                                errMap.put(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
                                return errMap;
                            }).collect(Collectors.toList());
            return responseEntity.body(errors);
        }

        return ResponseEntity.badRequest().build(); // ResponseEntity costructed by step
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<List<Map<String, String>>> handleBindErrors(MethodArgumentNotValidException exception) {
        List<Map<String, String>> errorList = exception.getFieldErrors().stream()
                                        .map(fieldError -> {
                                            Map<String, String> errorMap = new HashMap<>();
                                            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
                                            return errorMap;
                                        }).collect(Collectors.toList());
        
        return ResponseEntity.badRequest().body(errorList);
        
    }
}
