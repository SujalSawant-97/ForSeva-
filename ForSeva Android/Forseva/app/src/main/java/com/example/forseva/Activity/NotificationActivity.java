package com.example.forseva.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forseva.Client.RetrofitClient;
import com.example.forseva.DTO.ApiResponse;
import com.example.forseva.DTO.NotificationModel;
import com.example.forseva.R;
import com.example.forseva.Service.NotificationApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView rvNotifications;
    private NotificationAdapter adapter;
    private List<NotificationModel> notificationList = new ArrayList<>();
    private TextView tvEmpty;
    private ProgressBar pbNof;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        tvEmpty = findViewById(R.id.tvEmptyNotif);
        pbNof=findViewById(R.id.pbNotification);

        // Setup Toolbar with Back Button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Notifications");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        rvNotifications = findViewById(R.id.rvNotifications);
        rvNotifications.setLayoutManager(new LinearLayoutManager(this));

        adapter = new NotificationAdapter(notificationList);
        rvNotifications.setAdapter(adapter);

        fetchNotifications();
    }

    private void fetchNotifications() {
        NotificationApiService api = RetrofitClient.getRetrofitInstance(this)
                .create(NotificationApiService.class);
        pbNof.setVisibility(View.VISIBLE);

        // ERROR FIX: Use Call<ApiResponse<List<NotificationModel>>>
        api.getUserNotifications().enqueue(new Callback<ApiResponse<List<NotificationModel>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<NotificationModel>>> call, Response<ApiResponse<List<NotificationModel>>> response) {
                pbNof.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {

                    // Unwrap the ApiResponse
                    if (response.body().isSuccess()) {
                        List<NotificationModel> data = response.body().getData();

                        notificationList.clear();
                        if (data != null && !data.isEmpty()) {
                            notificationList.addAll(data);
                            tvEmpty.setVisibility(View.GONE);
                            rvNotifications.setVisibility(View.VISIBLE);
                        } else {
                            tvEmpty.setVisibility(View.VISIBLE);
                            rvNotifications.setVisibility(View.GONE);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(NotificationActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<NotificationModel>>> call, Throwable t) {
                Toast.makeText(NotificationActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
