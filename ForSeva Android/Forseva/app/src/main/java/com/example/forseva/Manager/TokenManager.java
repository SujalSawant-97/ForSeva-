package com.example.forseva.Manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import org.json.JSONObject;

public class TokenManager {
    private static final String PREF_NAME = "AppPrefs";
    private static final String KEY_TOKEN = "jwt_token";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public TokenManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Call this after a successful login
    public void saveToken(String token) {
        editor.putString(KEY_TOKEN, token);
        editor.apply(); // apply() is asynchronous and faster than commit()
    }

    // Call this when making Feign-like calls in Retrofit
    public String getToken() {
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    // Call this for Logout
    public void clearToken() {
        editor.remove(KEY_TOKEN);
        editor.apply();
    }
    public boolean isLoggedIn() {
        // Returns true if token exists, false otherwise
        return getToken() != null && !getToken().isEmpty();
    }
    public  boolean isTokenExpired( ) {
        try {
            String token=getToken();
            // JWT format: Header.Payload.Signature
            // We need the Payload (index 1)
            String[] split = token.split("\\.");
            if (split.length < 2) return true; // Invalid format, treat as expired

            // Decode the payload
            String payload = new String(Base64.decode(split[1], Base64.URL_SAFE));
            JSONObject jsonObject = new JSONObject(payload);

            // Get expiration time (exp is in seconds, System.currentTimeMillis is in ms)
            if (jsonObject.has("exp")) {
                long exp = jsonObject.getLong("exp");
                long currentTime = System.currentTimeMillis() / 1000;

                // If exp is less than current time, it's expired
                return exp < currentTime;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true; // If any error occurs, assume token is invalid/expired
    }
}
