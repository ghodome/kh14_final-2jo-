package com.art.error;

//내가 만든 예외 클래스
// RuntimeException을 자바에서는 unchecked exception이라고 부른다 <--> checked exception
// RuntimeException을 상속받으면 별도의 예외 처리 절차를 작성하지 않아도 됨
//public class TargetNotFoundException extends Exception{
public class TargetNotFoundException extends RuntimeException{
	public TargetNotFoundException () {}
	
	public TargetNotFoundException (String msg) {
		super(msg);
	}
	
}
