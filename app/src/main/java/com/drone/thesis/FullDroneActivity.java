package com.drone.thesis;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.drone.thesis.databinding.ActivityFullDroneBinding;

public class FullDroneActivity extends AppCompatActivity {

    ActivityFullDroneBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFullDroneBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
