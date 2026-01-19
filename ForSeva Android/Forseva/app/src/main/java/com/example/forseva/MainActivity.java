package com.example.forseva;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.forseva.Activity.HomeActivity;
import com.example.forseva.Activity.LoginActivity;
import com.example.forseva.Manager.TokenManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Delay for 3 seconds (3000 milliseconds)
        // Initialize your TokenManager
        TokenManager tokenManager = new TokenManager(this);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            // Check session
            if (tokenManager.isLoggedIn()&&tokenManager.isTokenExpired()) {
                // Token exists -> Go straight to Home
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
            } else {
                // No token -> Go to Login
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }

            // Destroy MainActivity so the "Back" button doesn't return to the Splash screen
            finish();

        }, 3000);
    }


}