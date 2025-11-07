package com.github.Dangerwind.springbootinertiareact.handler;

import io.github.inertia4j.spring.Inertia;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final Inertia inertia;

    @ExceptionHandler(ValidationException.class)
    public Object handleValidationException(ValidationException ex) {
        Map<String, String> errors = Map.of("title", ex.getMessage());
        return inertia.render("CreateProduct", Map.of("errors", errors));
    }

    @ExceptionHandler(NoIdException.class)
    public Object handleNoIdException(NoIdException ex) {
        Map<String, String> errors = Map.of("title", ex.getMessage());
         return inertia.render("CreateProduct", Map.of("errors", errors));
    }
}


