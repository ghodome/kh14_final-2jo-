package com.art.advice;

import java.io.IOException;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import lombok.extern.slf4j.Slf4j;


//JSON 내부에 빈 문자열을 null로 치환하는 도구(등록 필요 없고 DTO, VO에 선언)
@Slf4j
public class JsonEmptyStringToNullDeserializer extends JsonDeserializer<String>{
	@Override
	public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
		//JsonParser가 @JsonDeserialize가 붙은 필드 혹은 클래스의 내용을 읽는다
		String value = p.getText();
		boolean empty = value == null || value.isEmpty();
		return empty ? null : value;
	}
}
 