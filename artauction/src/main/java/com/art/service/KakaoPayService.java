package com.art.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.art.Configuration.KakaoPayProperties;
import com.art.vo.pay.KakaoPayApproveRequestVO;
import com.art.vo.pay.KakaoPayApproveResponseVO;
import com.art.vo.pay.KakaoPayCancelRequestVO;
import com.art.vo.pay.KakaoPayCancelResponseVO;
import com.art.vo.pay.KakaoPayOrderRequestVO;
import com.art.vo.pay.KakaoPayOrderResponseVO;
import com.art.vo.pay.KakaoPayReadyRequestVO;
import com.art.vo.pay.KakaoPayReadyResponseVO;


@Service
public class KakaoPayService {
	
	@Autowired
	private KakaoPayProperties kakaoPayProperties;//cid,secret
	
	@Autowired
	private RestTemplate template;//전송 도구
	
	@Autowired
	private HttpHeaders headers;//전송 헤더
	
	//결제 준비(ready)
	public KakaoPayReadyResponseVO ready(KakaoPayReadyRequestVO request) throws URISyntaxException{
		//2. 주소 생성
		URI uri = new URI("https://open-api.kakaopay.com/online/v1/payment/ready");//유효하지 않은 주소면 오류가 발생
		
		//4. 바디 생성
		Map<String,String> body = new HashMap<>();
		//body.put("key","value");
		body.put("cid",kakaoPayProperties.getCid());
		body.put("partner_order_id",request.getPartnerOrderId());//랜덤 번호 생성
		body.put("partner_user_id",request.getPartnerUserId());
		body.put("item_name",request.getItemName());
		body.put("quantity","1");
		body.put("total_amount",String.valueOf(request.getTotalAmount()));
		body.put("tax_free_amount","0");
		body.put("approval_url",request.getApprovalUrl() + "/" + request.getPartnerOrderId());
		body.put("cancel_url",request.getCancelUrl());
		body.put("fail_url",request.getFailUrl());
		
		//5. 통신 요청 정보 객체 생성(2+3+4)
		HttpEntity entity = new HttpEntity(body,headers);
		//6. 전송 후 응답 받기(2+5)
		KakaoPayReadyResponseVO response = 
				template.postForObject(uri,entity, KakaoPayReadyResponseVO.class);
		
		return response;
	}
	
	//결제 승인(approve)
	public KakaoPayApproveResponseVO approve(KakaoPayApproveRequestVO request) throws URISyntaxException{
		//2. 주소 생성
		URI uri = new URI("https://open-api.kakaopay.com/online/v1/payment/approve");//유효하지 않은 주소면 오류가 발생
		Map<String,String> body = new HashMap<>();
		//body.put("key","value");
		body.put("cid",kakaoPayProperties.getCid());
		body.put("partner_order_id",request.getPartnerOrderId());//랜덤 번호 생성
		body.put("partner_user_id",request.getPartnerUserId());
		body.put("tid",request.getTid());
		body.put("pg_token",request.getPgToken());		
		
		//5. 통신 요청 정보 객체 생성(2+3+4)
		HttpEntity entity = new HttpEntity(body,headers);
		KakaoPayApproveResponseVO response = 
				template.postForObject(uri, entity,KakaoPayApproveResponseVO.class);
		
		return response;
	}
	//결제 조회(order)
	public KakaoPayOrderResponseVO order(KakaoPayOrderRequestVO request) throws URISyntaxException{
		URI uri = new URI("https://open-api.kakaopay.com/online/v1/payment/order");
		
		Map<String,String> body = new HashMap<>();
		body.put("cid", kakaoPayProperties.getCid());
		body.put("tid", request.getTid());
		HttpEntity entity = new HttpEntity(body,headers);
		KakaoPayOrderResponseVO response = 
				template.postForObject(uri, entity,KakaoPayOrderResponseVO.class);
		return response;
	}
	
	//결제 취소(cancel)
		public KakaoPayCancelResponseVO cancel(KakaoPayCancelRequestVO request) throws URISyntaxException {
			URI uri = new URI("https://open-api.kakaopay.com/online/v1/payment/cancel");
			
			Map<String, String> body = new HashMap<>();
			body.put("cid", kakaoPayProperties.getCid());
			body.put("tid", request.getTid());
			body.put("cancel_amount", String.valueOf(request.getCancelAmount()));
			body.put("cancel_tax_free_amount", String.valueOf(request.getCancelTaxFreeAmount()));
			
//			HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);
			HttpEntity entity = new HttpEntity(body, headers);
			
			KakaoPayCancelResponseVO response = 
					template.postForObject(uri, entity, KakaoPayCancelResponseVO.class);
			
			return response;
		}
}
