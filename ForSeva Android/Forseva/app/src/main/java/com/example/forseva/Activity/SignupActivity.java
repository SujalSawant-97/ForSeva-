package com.example.forseva.Activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.forseva.Client.RetrofitClient;
import com.example.forseva.DTO.RegistrationRequest;
import com.example.forseva.R;
import com.example.forseva.Service.AuthApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    private EditText etEmail, etPass, etConfirm;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etEmail = findViewById(R.id.etEmailSignup);
        etPass = findViewById(R.id.etPassSignup);
        etConfirm = findViewById(R.id.etConfirmPass);
        btnSignUp = findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(v -> handleSignup());
    }

    private void handleSignup() {
        String email = etEmail.getText().toString().trim();
        String password = etPass.getText().toString().trim();
        String confirm = etConfirm.getText().toString().trim();

        // 1. Basic Validation
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirm)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Prepare Request (Assuming default role is CUSTOMER)
        RegistrationRequest regRequest = new RegistrationRequest(email, password, "USER");

        // 3. Call Backend
        AuthApiService apiService = RetrofitClient.getAuthenticatedService(this);
        apiService.register(regRequest).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(SignupActivity.this, "Registration Successful!", Toast.LENGTH_LONG).show();
                    // Redirect to Login
                    finish();
                } else {
                    Toast.makeText(SignupActivity.this, "Registration Failed: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(SignupActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
