package com.nextsteprecommendengine.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nextsteprecommendengine.api.config.CustomTokenEnhancer;
import com.nextsteprecommendengine.api.config.SecurityConstant;
import com.nextsteprecommendengine.api.constant.NextStepRecommendengineConstant;
import com.nextsteprecommendengine.api.dto.OauthClientDetailsDto;
import com.nextsteprecommendengine.api.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class Oauth2JWTTokenService {
    @Autowired
    private DefaultTokenServices tokenServices;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private JwtAccessTokenConverter accessTokenConverter;

    private static final String clientId = "abc_client";

    public OauthClientDetailsDto getOauthClientDetails(String clientId){
        try {
            String query = "SELECT client_id, scope, authorized_grant_types, access_token_validity, refresh_token_validity " +
                    "FROM oauth_client_details WHERE client_id = '" + clientId + "' LIMIT 1";
            OauthClientDetailsDto oauthClientDetailsDto = jdbcTemplate.queryForObject(query,
                    (resultSet, rowNum) -> new OauthClientDetailsDto(resultSet.getString("client_id"),
                            resultSet.getString("scope"), resultSet.getString("authorized_grant_types"),
                            resultSet.getInt("access_token_validity"), resultSet.getInt("refresh_token_validity")));
            return oauthClientDetailsDto;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public boolean checkCandidateGrantType(OauthClientDetailsDto oauthClientDetailsDto) {
        try {
            String[] grantTypesArray = oauthClientDetailsDto.getAuthorizedGrantTypes().split(",");
            for (String grantType : grantTypesArray) {
                if (grantType.trim().equalsIgnoreCase("candidate")) {
                    return true;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    public OAuth2AccessToken generateAccessToken(UserDetails userPrincipal, OauthClientDetailsDto oauthClientDetailsDto, String grantType ) {
        try {
            OAuth2Authentication authentication = convertAuthentication(userPrincipal, oauthClientDetailsDto,grantType);
            TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
            tokenEnhancerChain.setTokenEnhancers(Arrays.asList(new CustomTokenEnhancer(jdbcTemplate, objectMapper), accessTokenConverter));
            tokenServices.setTokenEnhancer(tokenEnhancerChain);
            tokenServices.setReuseRefreshToken(false);
            tokenServices.setSupportRefreshToken(true);
            tokenServices.setAccessTokenValiditySeconds(oauthClientDetailsDto.getAccessTokenValidInSeconds());
            tokenServices.setRefreshTokenValiditySeconds(oauthClientDetailsDto.getRefreshTokenValidInSeconds());
            OAuth2AccessToken token = tokenServices.createAccessToken(authentication);
            return token;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }


    private OAuth2Authentication convertAuthentication(UserDetails userPrincipal, OauthClientDetailsDto oauthClientDetailsDto, String grantType) {
        Map<String, String> requestParameters = new HashMap<>();
        requestParameters.put("grantType", grantType);
        requestParameters.put("phone", userPrincipal.getUsername());

        Set<GrantedAuthority> authorities = new HashSet<>(userPrincipal.getAuthorities());

        OAuth2Request oAuth2Request = new OAuth2Request(
                requestParameters,
                oauthClientDetailsDto.getClientId(),
                authorities,
                true,
                new HashSet<>(Arrays.asList(oauthClientDetailsDto.getScope().split(","))),
                null,
                null,
                null,
                null
        );

        return new OAuth2Authentication(oAuth2Request, new UsernamePasswordAuthenticationToken(userPrincipal, null, authorities));
    }
}
