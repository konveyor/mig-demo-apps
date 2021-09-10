package io.konveyor.demo.orders.exception.handler;

import io.konveyor.demo.orders.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ExceptionHandlingController {
	
	@ResponseStatus(value = HttpStatus.NOT_FOUND,
					reason = "Order not found")
	@ExceptionHandler(ResourceNotFoundException.class)
	public void resourceNotFound(ResourceNotFoundException e) {
		log.warn("Resource not found: " + e.getMessage());
	}

}
