package com.artificer.artifcerdelivery.delivery.tracking.exceptionHandler;

import com.artificer.artifcerdelivery.delivery.tracking.exception.EntidadeNaoEncontradaException;
import com.artificer.artifcerdelivery.delivery.tracking.exception.NegocioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String URL = "https://delivery-tracking.com/";
    @Autowired
    private MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {
        var problemDetails = ProblemDetail.forStatus(status);
        problemDetails.setTitle("Um ou mais campos do payload estão inválidos!");
        problemDetails.setType(URI.create("%serros/campos-invalidos".formatted(URL)));

        var fieldErros = ex.getBindingResult().getAllErrors().stream().collect(Collectors.toMap(
                objectError -> ((FieldError) objectError).getField(),
                objectError -> messageSource.getMessage(objectError, LocaleContextHolder.getLocale())));
        problemDetails.setProperty("fieldErros", fieldErros);

        return super.handleExceptionInternal(ex, problemDetails, headers, status, request);
    }

    @ExceptionHandler(NegocioException.class)
    public ProblemDetail handleNegocioException(NegocioException e) {
        var problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle(e.getMessage());
        problemDetail.setType(URI.create("%serros/regra-de-negocio-violada".formatted(URL)));
        return problemDetail;
    }

    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ProblemDetail handleEntidadeNaoEncontrada(NegocioException e) {
        var problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setTitle(e.getMessage());
        problemDetail.setType(URI.create("%serros/recurso-nao-encontrada".formatted(URL)));
        return problemDetail;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityViolation(DataIntegrityViolationException e) {
        var problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problemDetail.setTitle("Recurso a ser excluído está em uso!");
        problemDetail.setType(URI.create("%serros/recurso-em-uso".formatted(URL)));
        return problemDetail;
    }

}