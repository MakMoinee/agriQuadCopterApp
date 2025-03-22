package com.drone.thesis;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.drone.thesis.databinding.ActivityFullDroneBinding;
import com.drone.thesis.models.Drones;
import com.drone.thesis.services.DroneRequestService;
import com.github.MakMoinee.library.dialogs.MyDialog;
import com.github.MakMoinee.library.interfaces.LocalVolleyRequestListener;
import com.google.gson.Gson;

public class FullDroneActivity extends AppCompatActivity {

    ActivityFullDroneBinding binding;
    DroneRequestService service;
    Drones selectedDrone;
    MyDialog myDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFullDroneBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        service = new DroneRequestService(FullDroneActivity.this);
        selectedDrone = new Gson().fromJson(getIntent().getStringExtra("drones"), Drones.class);
        if (selectedDrone != null) {
            binding.txtDroneName.setText(selectedDrone.getDroneName());
        }
        myDialog = new MyDialog(FullDroneActivity.this);
        myDialog.setCustomMessage("Sending Request ...");
        setListeners();
    }

    private void setListeners() {
        binding.btnTakeOff.setOnClickListener(v -> {

            myDialog.show();
            service.takeOff(selectedDrone.getIp(), new LocalVolleyRequestListener() {
                @Override
                public void onSuccessString(String response) {
                    Toast.makeText(FullDroneActivity.this, "Successfully Send Request For Take Off", Toast.LENGTH_SHORT).show();
                    myDialog.dismiss();
                }

                @Override
                public void onError(Error error) {
                    Toast.makeText(FullDroneActivity.this, "Failed To Send Request For Take Off, Please Try Again Later", Toast.LENGTH_SHORT).show();
                    myDialog.dismiss();
                }
            });
        });
        binding.btnLanding.setOnClickListener(v -> {
            myDialog.show();
            service.landing(selectedDrone.getIp(), new LocalVolleyRequestListener() {
                @Override
                public void onSuccessString(String response) {
                    Toast.makeText(FullDroneActivity.this, "Successfully Send Request To Land", Toast.LENGTH_SHORT).show();
                    myDialog.dismiss();
                }

                @Override
                public void onError(Error error) {
                    Toast.makeText(FullDroneActivity.this, "Failed To Send Request To Land, Please Try Again Later", Toast.LENGTH_SHORT).show();
                    myDialog.dismiss();
                }
            });
        });
    }
}
