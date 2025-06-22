package com.nextsteprecommendengine.api.controller;

import com.nextsteprecommendengine.api.constant.NextStepRecommendengineConstant;
import com.nextsteprecommendengine.api.jwt.NextStepRecommendengineJwt;
import com.nextsteprecommendengine.api.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import java.util.Objects;

public class ABasicController {
    @Autowired
    private UserServiceImpl userService;


   /* public long getCurrentUser(){
        NextStepRecommendengineJwt nextStepRecommendengineJwt = userService.getAddInfoFromToken();
        return nextStepRecommendengineJwt.getAccountId();
    }

    public long getTokenId(){
        NextStepRecommendengineJwt nextStepRecommendengineJwt = userService.getAddInfoFromToken();
        return nextStepRecommendengineJwt.getTokenId();
    }

    public NextStepRecommendengineJwt getSessionFromToken(){
        return userService.getAddInfoFromToken();
    }

    public boolean isSuperAdmin(){
        NextStepRecommendengineJwt nextStepRecommendengineJwt = userService.getAddInfoFromToken();
        if(nextStepRecommendengineJwt !=null){
            return nextStepRecommendengineJwt.getIsSuperAdmin();
        }
        return false;
    }

    public boolean isShop(){
        NextStepRecommendengineJwt nextStepRecommendengineJwt = userService.getAddInfoFromToken();
        if(nextStepRecommendengineJwt !=null){
            return Objects.equals(nextStepRecommendengineJwt.getUserKind(), NextStepRecommendengineConstant.USER_KIND_MANAGER);
        }
        return false;
    }
    
    public boolean isEmployee(){
        NextStepRecommendengineJwt nextStepRecommendengineJwt = userService.getAddInfoFromToken();
        if(nextStepRecommendengineJwt !=null){
            return Objects.equals(nextStepRecommendengineJwt.getUserKind(), NextStepRecommendengineConstant.USER_KIND_EMPLOYEE);
        }
        return false;
    }*/


    public String getCurrentToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            OAuth2AuthenticationDetails oauthDetails =
                    (OAuth2AuthenticationDetails) authentication.getDetails();
            if (oauthDetails != null) {
                return oauthDetails.getTokenValue();
            }
        }
        return null;
    }
}
