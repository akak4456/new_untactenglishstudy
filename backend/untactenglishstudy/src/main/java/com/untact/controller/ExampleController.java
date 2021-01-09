package com.untact.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/**")
public class ExampleController {
	@GetMapping("/example")
	public ResponseEntity<String> getHttp(){
		return new ResponseEntity<>("SUCCESS!!!",HttpStatus.OK);
	}
}
