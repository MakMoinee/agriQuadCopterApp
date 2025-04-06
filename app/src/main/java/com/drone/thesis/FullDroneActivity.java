package com.drone.thesis;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.drone.thesis.databinding.ActivityFullDroneBinding;
import com.drone.thesis.models.Drones;
import com.drone.thesis.services.DroneRequestService;
import com.drone.thesis.services.ThermalFetcher;
import com.drone.thesis.views.JoyStickView;
import com.github.MakMoinee.library.dialogs.MyDialog;
import com.github.MakMoinee.library.interfaces.LocalVolleyRequestListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

public class FullDroneActivity extends AppCompatActivity implements ThermalFetcher.ThermalDataListener {

    ActivityFullDroneBinding binding;
    DroneRequestService service;
    Drones selectedDrone;
    MyDialog myDialog;
    private ThermalFetcher thermalFetcher;
    String picoIp = "";

    private float pitch = 1500;   // Default values for pitch, roll, throttle, and yaw
    private float roll = 1500;
    private float throttle = 1500;
    private float yaw = 1500;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFullDroneBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        service = new DroneRequestService(FullDroneActivity.this);
        selectedDrone = new Gson().fromJson(getIntent().getStringExtra("drone"), new TypeToken<Drones>() {
        }.getType());
        if (selectedDrone != null) {
            binding.txtDroneName.setText(selectedDrone.getDroneName());
            picoIp = selectedDrone.getIp();
        }
        myDialog = new MyDialog(FullDroneActivity.this);
        myDialog.setCustomMessage("Sending Request ...");

        thermalFetcher = new ThermalFetcher(this, picoIp, this);
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

        binding.btnDisarm.setOnClickListener(v -> {
            myDialog.show();
            service.disarm(selectedDrone.getIp(), new LocalVolleyRequestListener() {
                @Override
                public void onSuccessString(String response) {
                    Toast.makeText(FullDroneActivity.this, "Successfully Send Request To Disarm Drone", Toast.LENGTH_SHORT).show();
                    myDialog.dismiss();
                }

                @Override
                public void onError(Error error) {
                    Toast.makeText(FullDroneActivity.this, "Failed To Send Request To Disarm Drone, Please Try Again Later", Toast.LENGTH_SHORT).show();
                    myDialog.dismiss();
                }
            });
        });

        binding.btnArm.setOnClickListener(v -> {
            myDialog.show();
            service.arm(selectedDrone.getIp(), new LocalVolleyRequestListener() {
                @Override
                public void onSuccessString(String response) {
                    Toast.makeText(FullDroneActivity.this, "Successfully Send Request To Arm Drone", Toast.LENGTH_SHORT).show();
                    myDialog.dismiss();
                }

                @Override
                public void onError(Error error) {
                    Toast.makeText(FullDroneActivity.this, "Failed To Send Request To Arm Drone, Please Try Again Later", Toast.LENGTH_SHORT).show();
                    myDialog.dismiss();
                }
            });
        });

        binding.btnJoyLeft.setJoystickListener((x, y) -> {
            pitch = 1500 + x * 500;  // Map X to pitch (left-right)
            roll = 1500 + y * 500;   // Map Y to roll (up-down)
            sendControlCommand();
        });

        binding.btnJoyRight.setJoystickListener((x, y) -> {
            throttle = 1500 + x * 500;     // Map X to yaw (left-right)
            yaw = 1500 + y * 500;  // Map Y to throttle (up-down)
            sendControlCommand();
        });
    }

    private void sendControlCommand() {
        service.sendCommand(picoIp, pitch, roll, throttle,yaw, new LocalVolleyRequestListener() {
            @Override
            public void onSuccessString(String response) {

            }

            @Override
            public void onError(Error error) {
                Log.e("sendCommand_err", error.getLocalizedMessage());
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        thermalFetcher.startFetching();
    }

    @Override
    protected void onPause() {
        super.onPause();
        thermalFetcher.stopFetching();
    }

    @Override
    public void onThermalDataReceived(float[][] thermalData) {
        runOnUiThread(() -> binding.myHeatMap.updateHeatMap(thermalData));
    }
}
