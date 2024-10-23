package com.art.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.art.error.TargetNotFoundException;

import io.jsonwebtoken.ExpiredJwtException;

@RestControllerAdvice(basePackages = {"com.erp.restcontroller"})
public class ExceptionAdvice {
	
	// 두 가지 핸들러로 처리한다 (404, 500)
	@ExceptionHandler(TargetNotFoundException.class)
	public ResponseEntity<String> error404(){
		return ResponseEntity.status(404).body("대상 없음");
	}
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> error500(Exception e){
		e.printStackTrace();
		return ResponseEntity.internalServerError().body("server error");// 500 서버에러
	}
	@ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity<String> errorTokenExpire(Exception e){
		e.printStackTrace();
		return ResponseEntity.status(404).body("token expired");
	}

}

