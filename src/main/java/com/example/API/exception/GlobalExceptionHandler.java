package com.example.API.exception;

import com.example.API.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse> handleMissingParams() {
        ApiResponse apiResponse = new ApiResponse("Path param is incorrect",null);
        return ResponseEntity.badRequest().body(apiResponse);
    }
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse> noResourceFoundException(){
        ApiResponse apiResponse=new ApiResponse("No resource found exception",null);
        return ResponseEntity.badRequest().body(apiResponse);
    }
}
