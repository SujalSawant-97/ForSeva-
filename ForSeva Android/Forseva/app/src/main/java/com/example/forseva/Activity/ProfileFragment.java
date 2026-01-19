package com.example.forseva.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.forseva.Client.RetrofitClient;
import com.example.forseva.DTO.Address;
import com.example.forseva.DTO.ApiResponse;
import com.example.forseva.DTO.UserProfile;
import android.Manifest;
import com.example.forseva.R;
import com.example.forseva.Service.JobApiService;
import com.example.forseva.Service.UserApiService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private EditText etName, etEmail, etPhone, etCity, etState, etZipcode;
    private FusedLocationProviderClient fusedLocationClient;
    private double currentLat = 0.0;
    private double currentLng = 0.0;
    private ProgressBar progressBar;
    private UserApiService apiService;
    private String authToken;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        apiService = RetrofitClient.getRetrofitInstance(getContext())
                .create(UserApiService.class);
        etName = view.findViewById(R.id.etProfileName);
        etEmail = view.findViewById(R.id.etProfileEmail);
        etPhone = view.findViewById(R.id.etProfilePhone);
        etCity = view.findViewById(R.id.etCity);
        etState = view.findViewById(R.id.etState);
        etZipcode = view.findViewById(R.id.etZipcode);
        Button btnUpdate = view.findViewById(R.id.btnUpdateProfile);
        Button btnLogout=view.findViewById(R.id.btnLogout);
        progressBar = view.findViewById(R.id.progressBar);

        loadUserData();

        btnUpdate.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocationAndSave();
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            }
        });
        btnLogout.setOnClickListener(v -> performLogout());

        return view;
    }

    private void loadUserData() {
        progressBar.setVisibility(View.VISIBLE);
        UserApiService apiService = RetrofitClient.getRetrofitInstance(getContext())
                .create(UserApiService.class);

        // No token passed here!
        apiService.getProfile().enqueue(new Callback<ApiResponse<UserProfile>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserProfile>> call, Response<ApiResponse<UserProfile>> response) {
                progressBar.setVisibility(View.GONE);

                // UNWRAP: Check response -> Check body -> Check success flag/data
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<UserProfile> apiResponse = response.body();

                    // Assuming your ApiResponse has .getData() or similar
                    if (apiResponse.getData() != null) {
                        populateFields(apiResponse.getData());
                    } else {
                        Toast.makeText(getContext(), "Profile data is empty", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Failed to load profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<UserProfile>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateFields(UserProfile user) {
        etName.setText(user.getFullName());
        etEmail.setText(user.getEmail());
        etPhone.setText(user.getPhone());

        if (user.getAddress() != null) {
            etCity.setText(user.getAddress().getCity());
            etState.setText(user.getAddress().getState());
            etZipcode.setText(user.getAddress().getZipcode());
            // Store existing coords in case GPS fails during update
            currentLat = user.getAddress().getLatitude();
            currentLng = user.getAddress().getLongitude();
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocationAndSave() {
        // Fetch GPS silently
        fusedLocationClient.getLastLocation().addOnSuccessListener(requireActivity(), location -> {
            if (location != null) {
                currentLat = location.getLatitude();
                currentLng = location.getLongitude();
            }
            sendUpdateToBackend();
        });
    }

    private void sendUpdateToBackend() {
        progressBar.setVisibility(View.VISIBLE);

        // Construct objects
        Address address = new Address(
                etCity.getText().toString(),
                etState.getText().toString(),
                etZipcode.getText().toString(),
                currentLat,
                currentLng
        );

        UserProfile updatedProfile = new UserProfile(
                etName.getText().toString(),
                etEmail.getText().toString(),
                etPhone.getText().toString(),
                address
        );
        SharedPreferences prefs = requireActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("USER_NAME", etName.getText().toString());
// Overwrite the old address with the new one
        editor.putString("USER_CITY", etCity.getText().toString());
        editor.putString("USER_STATE", etState.getText().toString());
        editor.putString("USER_ZIP", etZipcode.getText().toString());
        editor.putString("USER_LAT", String.valueOf(currentLat));
        editor.putString("USER_LNG", String.valueOf(currentLng));
        String fullAddress = etCity.getText().toString() + ", " +
                etState.getText().toString() + " " +
                etZipcode.getText().toString();
        editor.putString("USER_FULL_ADDRESS", fullAddress);
        editor.apply();
        // Call Update API
        apiService.updateProfile(updatedProfile).enqueue(new Callback<ApiResponse<UserProfile>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserProfile>> call, Response<ApiResponse<UserProfile>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getContext(), "Profile Updated Successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Update Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<UserProfile>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void performLogout() {
        SharedPreferences pref = getActivity().getSharedPreferences("CignifiPrefs", Context.MODE_PRIVATE);
        pref.edit().clear().apply();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
