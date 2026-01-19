package com.example.forseva.Activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forseva.Model.ServiceCategory;
import com.example.forseva.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView rvCategories;
    private CategoryAdapter adapter;
    private SearchSuggestionAdapter suggestionAdapter;
    private List<ServiceCategory> fullServiceList;
    private RecyclerView rvSearchSuggestions;
    private EditText etSearch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 1. Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // 2. Initialize the RecyclerView
        rvCategories = view.findViewById(R.id.rvCategories);
        rvSearchSuggestions = view.findViewById(R.id.rvSearchSuggestions); // Make sure ID matches XML
        etSearch = view.findViewById(R.id.etHomeSearch);// ID was etHomeSearch in XML

        fullServiceList = new ArrayList<>();
        setupCategoryGrid();

        suggestionAdapter = new SearchSuggestionAdapter(getContext(), new ArrayList<>());
        rvSearchSuggestions.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSearchSuggestions.setAdapter(suggestionAdapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterSuggestions(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // 6. Editor Action Listener (Handle "Done" / "Search" on Keyboard)
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // Hide Dropdown
                    rvSearchSuggestions.setVisibility(View.GONE);

                    // Hide Keyboard
                    hideKeyboard(v);

                    // Optional: Clear focus so the cursor stops blinking
                    etSearch.clearFocus();
                    return true;
                }
                return false;
            }
        });

        // 7. Optional: Hide dropdown if user clicks away (Focus Change)
        etSearch.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                rvSearchSuggestions.setVisibility(View.GONE);
                hideKeyboard(v);
            }
        });


        return view;
    }

    private void setupCategoryGrid() {
        // Use getContext() instead of 'this' inside fragments
        //rvCategories = findViewById(R.id.rvCategories);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 4);
        rvCategories.setLayoutManager(layoutManager);
        //rvCategories.setNestedScrollingEnabled(false);

        List<ServiceCategory> categoryList = new ArrayList<>();
        categoryList.add(new ServiceCategory("Plumbing", R.drawable.ic_plumbing));
        categoryList.add(new ServiceCategory("Cleaning", R.drawable.ic_cleaning));
        categoryList.add(new ServiceCategory("Electrician", R.drawable.ic_electric));
        categoryList.add(new ServiceCategory("Painting", R.drawable.ic_painting));
        categoryList.add(new ServiceCategory("Beauty and Saloon",R.drawable.ic_beauty));
        categoryList.add(new ServiceCategory("Cook",R.drawable.ic_cook));
        categoryList.add(new ServiceCategory("Driver",R.drawable.ic_driver));
        categoryList.add(new ServiceCategory("Pest Control",R.drawable.ic_pest));
        // ... add the rest of your categories ...
        fullServiceList.clear();
        fullServiceList.addAll(categoryList);

        adapter = new CategoryAdapter(categoryList, getContext());
        rvCategories.setAdapter(adapter);

        adapter = new CategoryAdapter(categoryList, getContext());
        rvCategories.setAdapter(adapter);
    }
    private void filterSuggestions(String text) {
        if (text.isEmpty()) {
            rvSearchSuggestions.setVisibility(View.GONE);
        } else {
            List<ServiceCategory> filtered = new ArrayList<>();
            for (ServiceCategory item : fullServiceList) {
                if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                    filtered.add(item);
                }
            }
            if (!filtered.isEmpty()) {
                suggestionAdapter.updateList(filtered);
                rvSearchSuggestions.setVisibility(View.VISIBLE);
            } else {
                rvSearchSuggestions.setVisibility(View.GONE);
            }
        }
    }

    // Helper Method to Hide Keyboard
    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


}
