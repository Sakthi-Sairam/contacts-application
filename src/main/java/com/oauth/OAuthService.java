package com.oauth;

import com.exceptions.DaoException;
import com.models.User;
import com.utils.HttpClientUtil;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.JWTClaimsSet;

public class OAuthService {
	private final HttpClientUtil httpClientUtil = new HttpClientUtil();

	public void processOAuthCallback(String code, User currentUser) throws Exception {
		JSONObject tokens = exchangeCodeForTokens(code);
		String accessToken = tokens.getString("access_token");
		System.out.println("Access Token: " + accessToken);
		syncContacts(accessToken, currentUser.getUserId());
		addOAuthTokenEntryToDB(tokens, currentUser.getUserId());
		
	}
	public void syncContacts(String accessToken, int userId) throws Exception {
		String contactsData = fetchContacts(accessToken);
		String simplifiedContacts = simplifyContactsJson(contactsData);
		OAuthHelper.addOrUpdateTheContacts(simplifiedContacts, userId);
	}
	public JSONObject getAccessTokenWithRefreshToken(String refreshToken) throws IOException, InterruptedException {
		String params = String.format(
				"refresh_token=%s&client_id=%s&client_secret=%s&grant_type=refresh_token", refreshToken,
				OAuthConfig.CLIENT_ID, OAuthConfig.CLIENT_SECRET);
		HttpClientUtil httpClientUtil = new HttpClientUtil();
		JSONObject response = httpClientUtil.sendPostRequest(OAuthConfig.TOKEN_ENDPOINT, params);
		return response;
	}

	private JSONObject exchangeCodeForTokens(String code) throws IOException, InterruptedException, URISyntaxException {
		String params = String.format(
				"code=%s&client_id=%s&client_secret=%s&redirect_uri=%s&grant_type=authorization_code", code,
				OAuthConfig.CLIENT_ID, OAuthConfig.CLIENT_SECRET, OAuthConfig.REDIRECT_URI);

		HttpClientUtil httpClientUtil = new HttpClientUtil();
		JSONObject response = httpClientUtil.sendPostRequest(OAuthConfig.TOKEN_ENDPOINT, params);
		return response;
	}

	private String fetchContacts(String accessToken) throws Exception {
		String url = "https://people.googleapis.com/v1/people/me/connections?personFields=names,emailAddresses,phoneNumbers";
		return httpClientUtil.sendGetRequest(url, accessToken);
	}

	private String simplifyContactsJson(String contactsData) {
		JSONObject originalJson = new JSONObject(contactsData);
		JSONArray connections = originalJson.optJSONArray("connections");

		List<Map<String, String>> simplifiedContacts = new ArrayList<>();

		if (connections != null) {
			for (int i = 0; i < connections.length(); i++) {
				JSONObject connection = connections.getJSONObject(i);
				Map<String, String> contact = new HashMap<>();

				JSONArray names = connection.optJSONArray("names");
				if (names != null && names.length() > 0) {
					contact.put("name", names.getJSONObject(0).optString("displayName", ""));
				}

				JSONArray emails = connection.optJSONArray("emailAddresses");
				if (emails != null && emails.length() > 0) {
					contact.put("email", emails.getJSONObject(0).optString("value", ""));
				}

				JSONArray phones = connection.optJSONArray("phoneNumbers");
				if (phones != null && phones.length() > 0) {
					contact.put("phone", phones.getJSONObject(0).optString("value", ""));
				}

				contact.put("resourceName", connection.getString("resourceName"));

				if (!contact.isEmpty()) {
					simplifiedContacts.add(contact);
				}
			}
		}

		return new JSONObject().put("contacts", simplifiedContacts).toString(2);
	}
	
	private void addOAuthTokenEntryToDB(JSONObject tokens, int UserId) {
		try {
			String refreshToken = tokens.optString("refresh_token",null);
			String idToken = tokens.optString("id_token",null);
			if(refreshToken == null || idToken == null) return;
			JWT jwt = JWTParser.parse(idToken);
	        JWTClaimsSet claims = jwt.getJWTClaimsSet();
	        String email = claims.getStringClaim("email");
			OAuthDao.insertOAuthRecord(UserId,refreshToken,email);
		} catch (JSONException e) {
			return;
		} catch (DaoException | ParseException e) {
			e.printStackTrace();
		}
	}
}
