package com.art.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.art.error.TargetNotFoundException;

import io.jsonwebtoken.ExpiredJwtException;

@RestControllerAdvice(annotations = {RestController.class})
public class ExceptionAdvice {

	//TargetNotFoundException은 404로 처리
	//나머지는 500번으로 처리하되 메세지 제거(서버에만 출력)
	@ExceptionHandler(TargetNotFoundException.class)
	public ResponseEntity<String> error404(Exception e) {
		e.printStackTrace();
		//return ResponseEntity.ok().build();//200
		
//		return ResponseEntity.status(404).build();
		return ResponseEntity.status(404).body("target not found");
//		return ResponseEntity.notFound().build();//404
	}
	
	@ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity<String> errorTokenExpire(Exception e) {
		e.printStackTrace();
		return ResponseEntity.status(404).body("token expired");
	}
	
	@ExceptionHandler(Exception.class) 
	public ResponseEntity<String> error500(Exception e) {
		e.printStackTrace();
		//return ResponseEntity.status(500).build();
		//return ResponseEntity.internalServerError().build();
		return ResponseEntity.internalServerError().body("server error");
	}
	
	
}

