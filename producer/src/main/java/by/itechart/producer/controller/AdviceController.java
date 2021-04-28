package by.itechart.producer.controller;

import by.itechart.model.dto.ExceptionDto;
import org.springframework.amqp.AmqpConnectException;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_GATEWAY;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.GATEWAY_TIMEOUT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
public class AdviceController extends ResponseEntityExceptionHandler {

    private static final String EXCEPTION_MESSAGE = "Please, provide valid input data!";

    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<ExceptionDto> handleRunTimeException(final RuntimeException exception) {
        return new ResponseEntity<>(
                new ExceptionDto(INTERNAL_SERVER_ERROR.value(), exception.getMessage(),
                        Timestamp.valueOf(LocalDateTime.now()).toString()), INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {ListenerExecutionFailedException.class})
    public ResponseEntity<ExceptionDto> handleListenerExecutionFailedException(
            final ListenerExecutionFailedException exception) {
        return new ResponseEntity<>(
                new ExceptionDto(GATEWAY_TIMEOUT.value(), exception.getMessage(),
                        Timestamp.valueOf(LocalDateTime.now()).toString()), GATEWAY_TIMEOUT);
    }


    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(
            HttpMediaTypeNotAcceptableException ex,
            HttpHeaders headers, HttpStatus status,
            WebRequest request) {
        return new ResponseEntity<>(buildException(BAD_REQUEST.value(),
                ex.getClass().getName() + ": " + EXCEPTION_MESSAGE), BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(
            MissingPathVariableException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        return new ResponseEntity<>(buildException(BAD_REQUEST.value(),
                ex.getClass().getName() + ": " + EXCEPTION_MESSAGE), BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        return new ResponseEntity<>(buildException(BAD_REQUEST.value(),
                ex.getClass().getName() + ": " + EXCEPTION_MESSAGE), BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
            TypeMismatchException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        return new ResponseEntity<>(buildException(BAD_REQUEST.value(),
                ex.getClass().getName() + ": " + EXCEPTION_MESSAGE), BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        return new ResponseEntity<>(buildException(BAD_REQUEST.value(),
                ex.getClass().getName() + ": " + EXCEPTION_MESSAGE), BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        List<String> validationErrors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());
        return new ResponseEntity<>(buildException(BAD_REQUEST.value(), validationErrors.toString()), BAD_REQUEST);
    }

    @ExceptionHandler(value = {AmqpConnectException.class})
    public ResponseEntity<ExceptionDto> handleConnectException(final AmqpConnectException exception) {
        return new ResponseEntity<>(buildException(BAD_GATEWAY.value(), exception.getMessage()), BAD_GATEWAY);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<ExceptionDto> handleConstraintViolationException(final ConstraintViolationException e) {
        return new ResponseEntity<>(buildException(BAD_REQUEST.value(), e.getMessage()), BAD_REQUEST);
    }

    @ExceptionHandler(value = {HttpClientErrorException.class})
    public ResponseEntity<ExceptionDto> handleHttpClientErrorException(final HttpClientErrorException e) {
        return new ResponseEntity<>(buildException(BAD_REQUEST.value(), e.getMessage()), BAD_REQUEST);
    }

    @ExceptionHandler(value = {NumberFormatException.class})
    public ResponseEntity<ExceptionDto> handleWrongUsersInputException(final Exception e) {
        return new ResponseEntity<>(buildException(BAD_REQUEST.value(), EXCEPTION_MESSAGE), BAD_REQUEST);
    }

    private ExceptionDto buildException(final int errorCode, final String message) {
        return new ExceptionDto(errorCode, message, Timestamp.valueOf(LocalDateTime.now()).toString());
    }

}
