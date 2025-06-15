package com.nextsteprecommendengine.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class OauthClientDetailsDto {
    private String clientId;
    private String scope;
    private String authorizedGrantTypes;
    private int accessTokenValidInSeconds;
    private int refreshTokenValidInSeconds;
}
