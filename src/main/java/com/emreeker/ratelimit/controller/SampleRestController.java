package com.emreeker.ratelimit.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emreeker.ratelimit.annotation.RateLimit;

@RestController
public class SampleRestController {

	@RateLimit(value = 1.0)
	@GetMapping("/")
	public ResponseEntity<String> handleGetRequest() {
		String responseBody = "Hello world";
		return new ResponseEntity<>(responseBody, HttpStatus.OK);
	}
}
