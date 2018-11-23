package com.welflex.notes.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.welflex.notes.api.generated.Error;
import com.welflex.notes.exception.NoteNotFoundException;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@ControllerAdvice
public class ExceptionMapper extends ResponseEntityExceptionHandler {

  @ExceptionHandler(value = {NoteNotFoundException.class})
  public ResponseEntity<?> handleException(Exception e, HandlerMethod handlerMethod) {
    if (e instanceof NoteNotFoundException) {
      return handleNoteNotFoundException(NoteNotFoundException.class.cast(e), handlerMethod);
    }
    else {
      Error error = new Error().message("Unexpected Error:"  + e.getMessage());
      return new ResponseEntity<Error>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private ResponseEntity<?> handleNoteNotFoundException(NoteNotFoundException e,
    HandlerMethod handlerMethod) {
    ApiResponse apiResponse = getApiResponseForErrorCode(404, handlerMethod);
    Error error = new Error().message(apiResponse != null ? apiResponse.message() : "Note not found:" + e.getNoteId());
   
    return new ResponseEntity<Error>(error, HttpStatus.NOT_FOUND);
  }
  
  private ApiResponse getApiResponseForErrorCode(int errorCode, HandlerMethod method) {
    ApiResponses apiResponses = method.getMethodAnnotation(ApiResponses.class);
    
    if (apiResponses != null) {
      for (ApiResponse apiResponse : apiResponses.value()) {
        if (apiResponse.code() == errorCode) {
          return apiResponse;
        }
      }
    }
    
    return method.getMethodAnnotation(ApiResponse.class);
  }
}
