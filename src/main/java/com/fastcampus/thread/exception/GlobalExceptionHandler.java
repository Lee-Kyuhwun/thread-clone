package com.fastcampus.thread.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(ClinetErrorResponse.class)
    public ResponseEntity<?> handleCientError(ClinetErrorResponse e) {

        return new ResponseEntity<>(new ClientErrorResponse(e.getStatus(), e.getMessage()), e.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handle(MethodArgumentNotValidException e) {

        var errorMessage = e.getFieldErrors().stream()
                .map(fieldError -> (fieldError.getField() + " " + fieldError.getDefaultMessage()))
                .toList().toString();

        /**
         * e.getFieldErrors()
         * 역할: e는 주로 Spring의 BindingResult나 Errors 객체입니다.
         * getFieldErrors()는 유효성 검사(@Valid) 실패 시 발생한 모든 FieldError 객체의 리스트(List<FieldError>)를 반환합니다.
         *
         * 예시:
         * FieldError는 특정 필드의 오류 정보를 담고 있습니다.
         * 예) field: "email", defaultMessage: "이메일 형식이 올바르지 않습니다."
         * */
        return new ResponseEntity<>(new ClientErrorResponse(HttpStatus.BAD_REQUEST, errorMessage), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> HttpMessageNotReadableExceptionHandle(HttpMessageNotReadableException e) {


        return new ResponseEntity<>(new ClientErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handle(Exception e) {
        return ResponseEntity.internalServerError().build();

    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handle(RuntimeException e) {
        return new ResponseEntity<>(new ServerErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
