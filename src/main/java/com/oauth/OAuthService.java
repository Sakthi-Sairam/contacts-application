//package com.oauth;
//
//import com.models.User;
//import org.json.JSONObject;
//
//public class OAuthService {
//    private final OAuthHelper oauthHelper = new OAuthHelper();
//
//    public void processOAuthCallback(String code, User currentUser) throws Exception {
//        JSONObject tokens = exchangeCodeForTokens(code);
//        String accessToken = tokens.getString("access_token");
//        String contactsData = fetchContacts(accessToken);
//        String simplifiedContacts = JsonUtil.simplifyContactsJson(contactsData);
//        oauthHelper.addOrUpdateTheContacts(simplifiedContacts, currentUser.getUserId());
//    }
//
//    private JSONObject exchangeCodeForTokens(String code) throws Exception {
//        String params = String.format("code=%s&client_id=%s&client_secret=%s&redirect_uri=%s&grant_type=authorization_code",
//                code,
//                OAuthConfig.CLIENT_ID,
//                OAuthConfig.CLIENT_SECRET,
//                OAuthConfig.REDIRECT_URI);
//
//        return HttpUtil.sendPostRequest(OAuthConfig.TOKEN_ENDPOINT, params);
//    }
//}