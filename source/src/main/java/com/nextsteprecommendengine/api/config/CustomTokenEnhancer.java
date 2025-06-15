package com.nextsteprecommendengine.api.config;

import com.nextsteprecommendengine.api.dto.AccountForTokenDto;
import com.nextsteprecommendengine.api.utils.ZipUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class CustomTokenEnhancer implements TokenEnhancer {
    private JdbcTemplate jdbcTemplate;

    private ObjectMapper objectMapper;

    public CustomTokenEnhancer(JdbcTemplate jdbcTemplate, ObjectMapper objectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
    }
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Map<String, Object> additionalInfo = new HashMap<>();
        String username = authentication.getName();
        /*if(authentication.getOAuth2Request().getGrantType() != null){
            additionalInfo = getAdditionalInfo(username, authentication.getOAuth2Request().getGrantType());
        }else{
            String grantType = authentication.getOAuth2Request().getRequestParameters().get("grantType");
            if(grantType.equals(SecurityConstant.GRANT_TYPE_USER) ){
                String phone = authentication.getOAuth2Request().getRequestParameters().get("phone");
                additionalInfo = getAdditionalUserInfo(phone, grantType);
            } else if (grantType.equals(SecurityConstant.GRANT_TYPE_EMPLOYEE)) {
                String phone = authentication.getOAuth2Request().getRequestParameters().get("phone");
                additionalInfo = getAdditionalEmployeeInfo(phone, grantType);
            }else if (grantType.equals(SecurityConstant.GRANT_TYPE_CANDIDATE)) {
                String phone = authentication.getOAuth2Request().getRequestParameters().get("phone");
                additionalInfo = getAdditionalCandidateInfo(phone, grantType);
            }
        }
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);*/
        return accessToken;
    }

    /*private Map<String, Object> getAdditionalUserInfo(String phone, String grantType) {
        Map<String, Object> additionalInfo = new HashMap<>();
        AccountForTokenDto a = getUserByPhone(phone);

        if (a != null) {
            Long userId = a.getId();
            Long storeId = -1L;
            String kind = a.getKind() +  "";//token kind
            Long deviceId = -1L;// id cua thiet bi, lưu ở table device để get firebase url..
            String pemission = "<>";//empty string
            Integer userKind = a.getKind(); //loại user là admin hay là gì
            Integer tabletKind = -1;
            Long orderId = -1L;
            Boolean isSuperAdmin = a.getIsSuperAdmin();
            String tenantId = "";
            additionalInfo.put("user_id", a.getId());
            additionalInfo.put("user_kind", a.getKind());
            additionalInfo.put("grant_type", grantType == null ? SecurityConstant.GRANT_TYPE_PASSWORD : grantType);
            additionalInfo.put("tenant_info", tenantId);
            String DELIM = "|";
            String additionalInfoStr = ZipUtils.zipString(userId + DELIM
                    + storeId + DELIM
                    + kind + DELIM
                    + pemission + DELIM
                    + deviceId + DELIM
                    + userKind + DELIM
                    + tabletKind + DELIM
                    + orderId + DELIM
                    + isSuperAdmin + DELIM
                    + tenantId);
            additionalInfo.put("additional_info", additionalInfoStr);
        }
        return additionalInfo;
    }*/

    /*private Map<String, Object> getAdditionalEmployeeInfo(String phone, String grantType) {
        Map<String, Object> additionalInfo = new HashMap<>();
        AccountForTokenDto a = getUserByPhone(phone);

        if (a != null) {
            Long userId = a.getId();
            Long storeId = -1L;
            String kind = a.getKind() +  "";//token kind
            Long deviceId = -1L;// id cua thiet bi, lưu ở table device để get firebase url..
            String pemission = "<>";//empty string
            Integer userKind = a.getKind(); //loại user là admin hay là gì
            Integer tabletKind = -1;
            Long orderId = -1L;
            Boolean isSuperAdmin = a.getIsSuperAdmin();
            Long companyId = a.getCompanyId() != null ? a.getCompanyId() : -1L;
            additionalInfo.put("user_id", a.getId());
            additionalInfo.put("user_kind", a.getKind());
            additionalInfo.put("grant_type", grantType == null ? SecurityConstant.GRANT_TYPE_EMPLOYEE : grantType);
            additionalInfo.put("company_id", companyId);
            String DELIM = "|";
            String additionalInfoStr = ZipUtils.zipString(userId + DELIM
                    + storeId + DELIM
                    + kind + DELIM
                    + pemission + DELIM
                    + deviceId + DELIM
                    + userKind + DELIM
                    + tabletKind + DELIM
                    + orderId + DELIM
                    + isSuperAdmin + DELIM
                    + companyId);
            additionalInfo.put("additional_info", additionalInfoStr);
        }
        return additionalInfo;
    }*/

    /*private Map<String, Object> getAdditionalCandidateInfo(String phone, String grantType) {
        Map<String, Object> additionalInfo = new HashMap<>();
        AccountForTokenDto a = getUserByPhone(phone);

        if (a != null) {
            Long accountId = a.getId();
            Long storeId = -1L;
            String kind = a.getKind() + "";//token kind
            Long deviceId = -1L;// id cua thiet bi, lưu ở table device để get firebase url..
            String pemission = "<>";//empty string
            Integer userKind = a.getKind(); //loại user là admin hay là gì
            Integer tabletKind = -1;
            Long orderId = -1L;
            Boolean isSuperAdmin = a.getIsSuperAdmin();
            String tenantId = "";
            additionalInfo.put("user_id", accountId);
            additionalInfo.put("user_kind", a.getKind());
            additionalInfo.put("grant_type", grantType);
            additionalInfo.put("tenant_info", tenantId);
            String DELIM = "|";
            String additionalInfoStr = ZipUtils.zipString(accountId + DELIM
                    + storeId + DELIM
                    + kind + DELIM
                    + pemission + DELIM
                    + deviceId + DELIM
                    + userKind + DELIM
                    + tabletKind + DELIM
                    + orderId + DELIM
                    + isSuperAdmin + DELIM
                    + tenantId);
            additionalInfo.put("additional_info", additionalInfoStr);
        }
        return additionalInfo;
    }*/


    /*private Map<String, Object> getAdditionalInfo(String username, String grantType) {
        Map<String, Object> additionalInfo = new HashMap<>();
        AccountForTokenDto a = getAccountByUsername(username);

        if (a != null) {
            Long accountId = a.getId();
            Long storeId = -1L;
            String kind = a.getKind() + "";//token kind
            Long deviceId = -1L;// id cua thiet bi, lưu ở table device để get firebase url..
            String pemission = "<>";//empty string
            Integer userKind = a.getKind(); //loại user là admin hay là gì
            Integer tabletKind = -1;
            Long orderId = -1L;
            Boolean isSuperAdmin = a.getIsSuperAdmin();
            String tenantId = "";
            additionalInfo.put("user_id", accountId);
            additionalInfo.put("user_kind", a.getKind());
            additionalInfo.put("grant_type", grantType);
            additionalInfo.put("tenant_info", tenantId);
            String DELIM = "|";
            String additionalInfoStr = ZipUtils.zipString(accountId + DELIM
                    + storeId + DELIM
                    + kind + DELIM
                    + pemission + DELIM
                    + deviceId + DELIM
                    + userKind + DELIM
                    + username + DELIM
                    + tabletKind + DELIM
                    + orderId + DELIM
                    + isSuperAdmin + DELIM
                    + tenantId);
            additionalInfo.put("additional_info", additionalInfoStr);
        }
        return additionalInfo;
    }*/

    /*public AccountForTokenDto getAccountByUsername(String username) {
        try {
            String query = "SELECT id, kind, username, email, full_name, is_super_admin " +
                    "FROM db_account WHERE username = ? and status = 1 limit 1";
            log.debug(query);
            List<AccountForTokenDto> dto = jdbcTemplate.query(query, new Object[]{username},  new BeanPropertyRowMapper<>(AccountForTokenDto.class));
            if (dto.size() > 0)return dto.get(0);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }*/

   /* public AccountForTokenDto getUserByPhone(String phone) {
        try {
            String query = "SELECT a.id, a.kind, a.username, a.email, a.full_name, a.is_super_admin, e.company_id " +
                    "FROM db_account a " +
                    "LEFT JOIN db_employee e ON a.id = e.id " +
                    "WHERE (a.phone = ? OR a.email = ?) AND a.status = 1 LIMIT 1";
            log.debug(query);
            List<AccountForTokenDto> dto = jdbcTemplate.query(query, new Object[]{phone,phone},  new BeanPropertyRowMapper<>(AccountForTokenDto.class));
            if (dto.size() > 0)return dto.get(0);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }*/

    /*public List<Permission> getListPermissionByUserKind(Integer kind) {
        try{
            String query = "select * " +
                    "from db_permission p " +
                    "join db_permission_group g on p.id = g.permission_id " +
                    "join db_account a on a.group_id = g.group_id " +
                    "where a.kind = ?";
            return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Permission.class), kind);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }*/
}
