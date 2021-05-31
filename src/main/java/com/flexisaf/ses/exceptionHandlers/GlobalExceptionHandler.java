package com.flexisaf.ses.exceptionHandlers;

import com.flexisaf.ses.model.Response;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Iterator;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler
	public ResponseEntity<Object> handleRequestParameterValidationError(ConstraintViolationException exception) {

		Response response = new Response();

		StringBuilder errorMessage = new StringBuilder();
		Iterator<ConstraintViolation<?>> iterator = exception.getConstraintViolations().iterator();

		while (iterator.hasNext()) {
			ConstraintViolation<?> constraint = iterator.next();
			errorMessage.append(
					constraint.getInvalidValue().toString() + " " + constraint.getMessage() + System.lineSeparator());
		}

		response.setStatus(Response.FAILED);
		response.setMessage(errorMessage.toString());

		return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler
	public ResponseEntity<Object> handleRequestBodyValidationError(MethodArgumentNotValidException exception) {

		Response response = new Response();
		StringBuilder errorMessage = new StringBuilder();
		BindingResult result = exception.getBindingResult();
		for (FieldError error : result.getFieldErrors()) {
			errorMessage.append(error.getField() + " " + error.getDefaultMessage() + System.lineSeparator()
					+ System.lineSeparator());
		}

		response.setStatus(Response.FAILED);
		response.setMessage(errorMessage.toString());
		return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler
	public ResponseEntity<Object> handleAxamansardException(FlexiException exception) {
		Response response = new Response();
		response.setMessage(exception.getMessage());
		response.setStatus(Response.FAILED);
		return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<Object> handleMissingParams(MissingServletRequestParameterException ex) {
		Response response = new Response();
		String errorMessage = ex.getMessage();

		response.setStatus(Response.FAILED);
		response.setMessage(errorMessage);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Object> handleInvalidFormartException(InvalidFormatException ex) {
		Response response = new Response();
		String errorMessage = ex.getMessage();

		response.setStatus(Response.FAILED);
		response.setMessage(errorMessage);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MissingServletRequestPartException.class)
	public ResponseEntity<Object> handleMissingParts(MissingServletRequestPartException ex) {
		Response response = new Response();
		String errorMessage = ex.getMessage();

		response.setStatus(Response.FAILED);
		response.setMessage(errorMessage);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<Object> illegalArgumentException(MethodArgumentTypeMismatchException ex) {
		Response response = new Response();
		String errorMessage = ex.getMessage();

		response.setStatus(Response.FAILED);
		response.setMessage(errorMessage);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler
	public ResponseEntity<Object> handleSQLValidationError(SQLIntegrityConstraintViolationException exception) {

		Response response = new Response();

		StringBuilder errorMessage = new StringBuilder();
		Iterator<Throwable> iterator = exception.iterator();

		while (iterator.hasNext()) {
			Throwable e = iterator.next();
			errorMessage.append(e.getMessage() + System.lineSeparator());
		}
		response.setStatus(Response.FAILED);
		response.setMessage(errorMessage.toString());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

}