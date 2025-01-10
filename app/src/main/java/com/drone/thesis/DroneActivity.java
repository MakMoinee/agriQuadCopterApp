package com.drone.thesis;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.drone.thesis.databinding.ActivityDroneBinding;

public class DroneActivity extends AppCompatActivity {
    ActivityDroneBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDroneBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
