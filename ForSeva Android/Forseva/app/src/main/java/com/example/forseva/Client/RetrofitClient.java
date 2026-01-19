package com.example.forseva.Client;

import android.content.Context;

import com.example.forseva.Manager.TokenManager;
import com.example.forseva.Service.AuthApiService;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    //private static final String BASE_URL = "http://10.0.2.2:8080/"; // Localhost for Emulator
    private static Retrofit retrofit = null;
    // 1. Create a method that returns the Retrofit Object
    public static Retrofit getRetrofitInstance(Context context) {
        TokenManager tokenManager = new TokenManager(context);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request.Builder builder = original.newBuilder();
                    String token = tokenManager.getToken();
                    if (token != null) {
                        builder.header("Authorization", "Bearer " + token);
                    }
                    return chain.proceed(builder.build());
                })
                .build();

        // Re-use the same retrofit instance if it already exists
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.117.112.7:8080/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    public static AuthApiService getAuthenticatedService(Context context) {
        TokenManager tokenManager = new TokenManager(context);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request.Builder builder = original.newBuilder();

                    String token = tokenManager.getToken();
                    if (token != null) {
                        // Attach the token to the header
                        builder.header("Authorization", "Bearer " + token);
                    }

                    return chain.proceed(builder.build());
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.117.112.7:8080/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(AuthApiService.class);
    }
}
