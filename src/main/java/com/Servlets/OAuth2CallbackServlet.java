package com.Servlets;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.filters.SessionFilter;
import com.models.User;
import com.oauth.OAuthConfig;
import com.oauth.OAuthDao;
import com.oauth.OAuthHelper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/oauth2callback")
public class OAuth2CallbackServlet extends HttpServlet {
    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String code = request.getParameter("code");
        if (code == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing 'code' parameter");
            return;
        }
        
        try {
            JSONObject tokens = exchangeCodeForTokens(code);
            System.out.println(tokens);
            String accessToken = tokens.getString("access_token");
            System.out.println("Access Token: "+accessToken);
            String contactsData = fetchContacts(accessToken);
            String simplifiedContacts = simplifyContactsJson(contactsData);            

//            System.out.println(simplifiedContacts);

            User currentUser = (User) SessionFilter.getCurrentUser();
//            OAuthDao.insertContacts(simplifiedContacts,currentUser.getUserId());
            OAuthHelper.addOrUpdateTheContacts(simplifiedContacts,currentUser.getUserId());
            
            response.sendRedirect("/contacts");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing request");
        }
    }

    private JSONObject exchangeCodeForTokens(String code) throws IOException, InterruptedException, URISyntaxException {
        String params = String.format("code=%s&client_id=%s&client_secret=%s&redirect_uri=%s&grant_type=authorization_code",
                code,
                OAuthConfig.CLIENT_ID,
                OAuthConfig.CLIENT_SECRET,
                OAuthConfig.REDIRECT_URI);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(OAuthConfig.TOKEN_ENDPOINT))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(params))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return new JSONObject(response.body());
    }

    private String fetchContacts(String accessToken) throws IOException, InterruptedException, URISyntaxException {
        String url = "https://people.googleapis.com/v1/people/me/connections?personFields=names,emailAddresses,phoneNumbers";
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    private String simplifyContactsJson(String contactsData) {
        JSONObject originalJson = new JSONObject(contactsData);
        JSONArray connections = originalJson.optJSONArray("connections");
        
        List<Map<String, String>> simplifiedContacts = new ArrayList<>();
        
        if (connections != null) {
            for (int i = 0; i < connections.length(); i++) {
                JSONObject connection = connections.getJSONObject(i);
                Map<String, String> contact = new HashMap<>();
                
                // Extract name
                JSONArray names = connection.optJSONArray("names");
                if (names != null && names.length() > 0) {
                    contact.put("name", names.getJSONObject(0).optString("displayName", ""));
                }
                
                // Extract email
                JSONArray emails = connection.optJSONArray("emailAddresses");
                if (emails != null && emails.length() > 0) {
                    contact.put("email", emails.getJSONObject(0).optString("value", ""));
                }
                
                // Extract phone
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
}