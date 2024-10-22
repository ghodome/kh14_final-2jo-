package com.art.vo.pay;




import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoPaySelectedCardInfoVO {

private String cardBin;//카드 BIN
private int installMonth;//할부 개월 수
private String installmentType;//할부 유형(24.02.01일부터 제공)- CARD_INSTALLMENT: 업종 무이자- SHARE_INSTALLMENT: 분담 무이자
private String cardCorpName;//카드사 정보
private String interestFreeInstall;//무이자할부 여부(Y/N)
}
