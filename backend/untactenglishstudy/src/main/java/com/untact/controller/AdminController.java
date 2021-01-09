package com.untact.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.untact.service.admin.AdminService;

@RestController
public class AdminController {
	//원래는 관리자인지 확인하는 과정을 security config에 추가해야함
	//일단은 생략
	
	@Autowired
	private AdminService adminService;
	@PostMapping("/admin/setupquiz")
	public ResponseEntity<String> setup(){
		if(adminService.setup())
			return new ResponseEntity<>("success",HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
}
