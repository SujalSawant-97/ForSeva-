package com.example.forseva.Activity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forseva.DTO.ApiResponse;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import android.Manifest;
import com.google.android.gms.tasks.OnSuccessListener;
import android.location.Location;

import com.example.forseva.Client.RetrofitClient;
import com.example.forseva.Model.WorkerModel;
import com.example.forseva.R;
import com.example.forseva.Service.WorkerApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkerSearchActivity extends AppCompatActivity {

    private RecyclerView rvWorkers;
    private WorkerAdapter adapter;
    private List<WorkerModel> workerList = new ArrayList<>();
    private ProgressBar progressBar;
    private TextView tvHeaderInfo, tvNoResults;

    private FusedLocationProviderClient fusedLocationClient;
    private String mainCategory, subServiceTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_search);

        // 1. Initialize Views
        rvWorkers = findViewById(R.id.rvWorkerResults);
        progressBar = findViewById(R.id.searchProgressBar);
        tvHeaderInfo = findViewById(R.id.tvSearchHeader);
        tvNoResults = findViewById(R.id.tvNoWorkersFound);
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // 2. Get Data from Intent (passed from SubServiceActivity)
        mainCategory = getIntent().getStringExtra("SERVICE_TYPE");
        subServiceTask = getIntent().getStringExtra("JOB_DESCRIPTION");

        if (mainCategory != null && subServiceTask != null) {
            tvHeaderInfo.setText(mainCategory + "s for " + subServiceTask);
        }

        // 3. Setup RecyclerView
        rvWorkers.setLayoutManager(new LinearLayoutManager(this));
        adapter = new WorkerAdapter(workerList,subServiceTask,mainCategory, this);
        rvWorkers.setAdapter(adapter);

        // 4. Initialize Location Service
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Start the process
        checkLocationPermissionAndSearch();
    }

    private void checkLocationPermissionAndSearch() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
        } else {
            getCurrentLocationAndFetchWorkers();
        }
    }

    private void getCurrentLocationAndFetchWorkers() {
        progressBar.setVisibility(View.VISIBLE);

        try {
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    performApiSearch(location.getLatitude(), location.getLongitude());
                } else {
                    // If location is null, use a default city center or show error
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Unable to get GPS location", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void performApiSearch(double lat, double lng) {
        WorkerApiService api = RetrofitClient.getRetrofitInstance(this).create(WorkerApiService.class);
        progressBar.setVisibility(View.VISIBLE);
        // Making the call with your ApiResponse wrapper
        api.getFilteredWorkers(mainCategory, lat, lng)
                .enqueue(new Callback<ApiResponse<List<WorkerModel>>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<List<WorkerModel>>> call, Response<ApiResponse<List<WorkerModel>>> response) {
                        progressBar.setVisibility(View.GONE);

                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().isSuccess()) {
                                List<WorkerModel> data = response.body().getData();

                                workerList.clear();
                                if (data != null && !data.isEmpty()) {
                                    workerList.addAll(data);
                                    tvNoResults.setVisibility(View.GONE);
                                } else {
                                    tvNoResults.setVisibility(View.VISIBLE);
                                }
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(WorkerSearchActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<List<WorkerModel>>> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(WorkerSearchActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocationAndFetchWorkers();
        } else {
            Toast.makeText(this, "Location permission required to find nearby workers", Toast.LENGTH_LONG).show();
        }
    }
}
