package com.fastcampus.thread.exception;


import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_EMPTY) // null인 경우 json으로 변환하지 않음
public record ClientErrorResponse(HttpStatus status, String message) {


}
