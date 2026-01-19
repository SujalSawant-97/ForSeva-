package com.example.forseva.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forseva.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SubServiceActivity extends AppCompatActivity {
    private RecyclerView rvSubServices;
    private SubServiceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_service);

        // 1. Get the category passed from HomeActivity
        String category = getIntent().getStringExtra("SERVICE_NAME");

        rvSubServices = findViewById(R.id.rvSubServices);
        rvSubServices.setLayoutManager(new LinearLayoutManager(this));

        // 2. Load data (Ideally from an API call)
        List<String> subServices = getMockData(category);

        // 3. Set Adapter
        adapter = new SubServiceAdapter(subServices, task -> {
            Intent intent = new Intent(this, WorkerSearchActivity.class);

            // PASS BOTH PIECES OF DATA
            intent.putExtra("SERVICE_TYPE", category);      // Main Category (e.g., Electrician)
            intent.putExtra("JOB_DESCRIPTION", task);       // Sub-service (e.g., AC Repair)

            startActivity(intent);
        });
        rvSubServices.setAdapter(adapter);
    }

    private List<String> getMockData(String category) {
        if ("Electrician".equals(category)) {
            return Arrays.asList("AC Repair", "TV Installation", "Fan Repair", "Switch Replacement");
        } else if ("Plumbing".equals(category)) {
            return Arrays.asList("Tap Leakage", "Toilet Repair", "Pipe Blockage");
        } else if ("Pest Control".equals(category)) {
            return Arrays.asList("Termites Pest Control", "Rodents Termination", "Cockroaches Extermination","Mosquitoes Control","Bed bugs Removal","Whild Life Management");
        }else if ("Cleaning".equals(category)) {
            return Arrays.asList("Home Clean", "Toilet Clean", "Bathroom Clean","Car Clean");
        }else if ("Painting".equals(category)) {
            return Arrays.asList("Wall Paint", "House Paint", "Furniture Paint");
        }else if ("Driver".equals(category)) {
            return Arrays.asList("Part Time", "Trip", "One Day");
        }else if ("Cook".equals(category)) {
            return Arrays.asList("Breakfast", "Lunch", "Diner","Party Food","Function Food");
        }else if ("Beauty and Saloon".equals(category)) {
            return Arrays.asList("Hair Cut", "Hair Colour", "Makeup");
        }
        return new ArrayList<>();
    }
}
