package com.drone.thesis;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.drone.thesis.databinding.ActivityMainBinding;
import com.drone.thesis.models.Users;
import com.drone.thesis.preference.LocalPref;
import com.drone.thesis.services.UserService;
import com.github.MakMoinee.library.common.MapForm;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    UserService userService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userService = new UserService(MainActivity.this);
        int id = new LocalPref(MainActivity.this).getIntItem("id");
        Log.e("ID >>>>", Integer.toString(id));
        if (id != 0) {
            Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        } else {
            setListeners();
        }
    }

    private void setListeners() {
        binding.txtCreateAccount.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CreateAccountActivity.class);
            startActivity(intent);
        });
        binding.btnLogin.setOnClickListener(v -> {
            String username = binding.editUn.getText().toString().trim();
            String password = binding.editPw.getText().toString().trim();
            if (username.equals("") || password.equals("")) {
                Toast.makeText(MainActivity.this, "Please Don't Leave Empty Fields", Toast.LENGTH_SHORT).show();
            } else {
                Users loggedInUser = userService.getUserRecord(username, password);
                if (loggedInUser != null) {
                    Log.e("here", new Gson().toJson(loggedInUser));
                    new LocalPref(MainActivity.this).storeLogin(MapForm.convertObjectToMap(loggedInUser));
                    Toast.makeText(MainActivity.this, "Successfully Login", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "Wrong Username or Password", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}