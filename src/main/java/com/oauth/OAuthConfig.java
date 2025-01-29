package com.oauth;

import com.utils.ConfigUtil;

public class OAuthConfig {
    public static final String CLIENT_ID = ConfigUtil.get("CLIENT_ID");
    public static final String CLIENT_SECRET = ConfigUtil.get("CLIENT_SECRET");
    public static final String REDIRECT_URI = ConfigUtil.get("REDIRECT_URI");
    public static final String AUTH_ENDPOINT = ConfigUtil.get("AUTH_ENDPOINT");
    public static final String TOKEN_ENDPOINT = ConfigUtil.get("TOKEN_ENDPOINT");
    public static final String PEOPLE_ENDPOINT = ConfigUtil.get("PEOPLE_ENDPOINT");
//    public static final String SCOPE = "email profile https://www.googleapis.com/auth/contacts.readonly";
    public static final String SCOPE = ConfigUtil.get("SCOPE");

    
    // Utility method to generate secure random state
//    public static String generateState() {
//        SecureRandom secureRandom = new SecureRandom();
//        byte[] state = new byte[32];
//        secureRandom.nextBytes(state);
//        return Base64.getUrlEncoder().withoutPadding().encodeToString(state);
//    }
}