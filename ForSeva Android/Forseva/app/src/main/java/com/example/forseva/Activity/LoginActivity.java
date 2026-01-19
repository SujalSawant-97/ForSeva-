package com.example.forseva.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.forseva.Client.RetrofitClient;
import com.example.forseva.DTO.ApiResponse;
import com.example.forseva.DTO.LoginRequest;
import com.example.forseva.DTO.LoginResponse;
import com.example.forseva.Manager.TokenManager;
import com.example.forseva.R;
import com.example.forseva.Service.AuthApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnSignIn;
    private TextView tvRedirectToSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        tvRedirectToSignup = findViewById(R.id.tvSignUp);

        tvRedirectToSignup.setOnClickListener(v -> {
            // 1. Define the Intent (From current activity to Target activity)
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);

            // 2. Start the Activity
            startActivity(intent);
        });

        btnSignIn.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            // This is where we will call your backend API Gateway
            //use this to receive token in header
            //performLogin(email, password);
            // use this to receive token in body
            performLogin2(email,password);
        });
    }

//    private void performLogin(String email, String password) {
//        LoginRequest request = new LoginRequest(email, password);
//        AuthApiService service = RetrofitClient.getAuthenticatedService(this);
//
//        service.login(request).enqueue(new Callback<ApiResponse<Void>>() {
//            @Override
//            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
//                // 1. Check if the HTTP status is 200 OK
//                if (response.isSuccessful() && response.body() != null) {
//                    ApiResponse<Void> apiResponse = response.body();
//
//
//                    // 2. Check your custom 'success' flag from the backend
//                    if (apiResponse.isSuccess()) {
//                        // Success! Get the token from the header as discussed before
//                        String jwt = response.headers().get("Authorization");
//
//                        TokenManager tokenManager = new TokenManager(LoginActivity.this);
//                        tokenManager.saveToken(jwt);
//
//
//                        // Navigate to Home Page
//                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
//                        startActivity(intent);
//                        finish();
//                    } else {
//                        Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//
//                @Override
//                public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
//                    Toast.makeText(LoginActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
//                }
//            });
//    }

    // copy of above method but token is returned in boby insted of header
    private void performLogin2(String email, String password) {
        LoginRequest request = new LoginRequest(email, password);
        AuthApiService service = RetrofitClient.getAuthenticatedService(this);

        service.logind(request).enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                // 1. Check if the HTTP status is 200 OK
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<String> apiResponse = response.body();


                    // 2. Check your custom 'success' flag from the backend
                    if (apiResponse.isSuccess()) {
                        // Success! Get the token from the header as discussed before
                        //String jwt = response.headers().get("Authorization");
                        String t=apiResponse.getData();

                        TokenManager tokenManager = new TokenManager(LoginActivity.this);
                        tokenManager.saveToken(t);

                        // Navigate to Home Page
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
