package com.example.forseva.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forseva.Client.RetrofitClient;
import com.example.forseva.DTO.ApiResponse;
import com.example.forseva.Model.ServiceCategory;
import com.example.forseva.R;
import com.example.forseva.Service.NotificationApiService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private ImageView ivBell;
    private View notificationBadge;
    private BottomNavigationView bottomNav;

    private TextView tvUserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ivBell = findViewById(R.id.ivNotificationBell);
        notificationBadge = findViewById(R.id.notificationBadge);
        bottomNav = findViewById(R.id.bottomNav);

        tvUserName = findViewById(R.id.tvUserName);
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);

        String name=   prefs.getString("USER_NAME", "");

        if (name != null && !name.isEmpty()) {
            tvUserName.setText(name); // Displays "Sujal"
        } else {
            tvUserName.setText("User");
        }
        // Load HomeFragment by default
        loadFragment(new HomeFragment());

        if (getIntent().hasExtra("TARGET_FRAGMENT")) {
            String target = getIntent().getStringExtra("TARGET_FRAGMENT");
            if ("PROFILE".equals(target)) {
                // Assuming your Profile menu ID is R.id.nav_profile
                bottomNav.setSelectedItemId(R.id.nav_profile);
            }
        }


        ImageView ivBell = findViewById(R.id.ivNotificationBell);
        ivBell.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, NotificationActivity.class);
            startActivity(intent);
        });

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                loadFragment(new HomeFragment());
                return true;
            } else if (id == R.id.nav_profile) {
                loadFragment(new ProfileFragment());
                return true;
            } else if(id==R.id.nav_bookings){
                loadFragment(new BookingsFragment());
                return true;
            }
            return false;
        });
        checkUnreadNotifications();

    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent); // Update the intent

        if (intent.hasExtra("TARGET_FRAGMENT")) {
            String target = intent.getStringExtra("TARGET_FRAGMENT");
            if ("PROFILE".equals(target)) {
               // BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
                bottomNav.setSelectedItemId(R.id.nav_profile);
            }
        }
    }
    private void checkUnreadNotifications() {
        NotificationApiService api = RetrofitClient.getRetrofitInstance(this)
                .create(NotificationApiService.class);

        api.getUnreadCount().enqueue(new Callback<ApiResponse<Integer>>() {
            @Override
            public void onResponse(Call<ApiResponse<Integer>> call, Response<ApiResponse<Integer>> response) {
                // Check if response exists and backend success flag is true
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {

                    int unreadCount = response.body().getData();

                    if (unreadCount > 0) {
                        notificationBadge.setVisibility(View.VISIBLE);
                        // Optional: Set the text if your badge shows the number
                        // notificationBadge.setText(String.valueOf(unreadCount));
                    } else {
                        // CRITICAL: Hide the badge if the count is 0
                        notificationBadge.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Integer>> call, Throwable t) {
                // Log for debugging but don't show toast to user for background tasks
                Log.e("NotifCheck", "Failed to fetch count: " + t.getMessage());
            }
        });
    }
private void loadFragment(Fragment fragment) {
    getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit();
}
}
