package com.algaworks.fastpay.application.handler;

import com.algaworks.fastpay.application.config.FastPayProperties;
import com.algaworks.fastpay.application.exception.AccessDeniedOnResourceException;
import com.algaworks.fastpay.application.exception.BusinessException;
import com.algaworks.fastpay.application.exception.CreditCardAlreadyAssignedCustomerException;
import com.algaworks.fastpay.application.exception.DomainEntityNotFound;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;
    private final FastPayProperties fastPayProperties;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        problemDetail.setTitle("Invalid fields");
        problemDetail.setDetail("One or more fields are invalid");
        problemDetail.setType(URI.create("/errors/invalid-fields"));

        var fields = ex.getBindingResult().getAllErrors().stream()
                .collect(Collectors.toMap(error -> ((FieldError) error).getField(),
                        error -> messageSource.getMessage(error, LocaleContextHolder.getLocale())));

        problemDetail.setProperty("fields", fields);

        return super.handleExceptionInternal(ex, problemDetail, headers, status, request);
    }

    @ExceptionHandler(BusinessException.class)
    public ProblemDetail handle(BusinessException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(400);
        problemDetail.setTitle(e.getMessage());
        problemDetail.setType(URI.create(fastPayProperties.getHostname() +"/errors/business"));

        return problemDetail;
    }

    @ExceptionHandler(CreditCardAlreadyAssignedCustomerException.class)
    public ProblemDetail handle(CreditCardAlreadyAssignedCustomerException e, HttpServletResponse response) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(409);
        problemDetail.setTitle(e.getMessage());
        problemDetail.setType(URI.create(fastPayProperties.getHostname() +"/errors/conflict"));
        problemDetail.setProperty("creditCardId", e.getCardId());

        response.addHeader("Location", "/api/v1/credit-cards/" + e.getCardId());

        return problemDetail;
    }

    @ExceptionHandler(DomainEntityNotFound.class)
    public ProblemDetail handle(DomainEntityNotFound e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(404);
        problemDetail.setTitle(e.getMessage());
        problemDetail.setType(URI.create(fastPayProperties.getHostname() +"/errors/not-found"));

        return problemDetail;
    }

    @ExceptionHandler(AccessDeniedOnResourceException.class)
    public ProblemDetail handle(AccessDeniedOnResourceException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(403);
        problemDetail.setTitle(e.getMessage());
        problemDetail.setType(URI.create(fastPayProperties.getHostname() +"/errors/denied"));

        return problemDetail;
    }

}