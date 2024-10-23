package com.art.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenResponseVO {
    private String accessToken;
    private String refreshToken;
}