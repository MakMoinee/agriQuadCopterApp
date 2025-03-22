package com.drone.thesis;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.drone.thesis.adapters.DroneAdapter;
import com.drone.thesis.databinding.ActivityDroneBinding;
import com.drone.thesis.models.Drones;
import com.drone.thesis.preference.LocalPref;
import com.drone.thesis.services.DroneRequestService;
import com.drone.thesis.services.DroneService;
import com.github.MakMoinee.library.interfaces.DefaultBaseListener;
import com.github.MakMoinee.library.interfaces.DefaultEventListener;
import com.github.MakMoinee.library.interfaces.LocalVolleyRequestListener;
import com.github.MakMoinee.library.models.LocalVolleyRequestBody;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

public class DroneActivity extends AppCompatActivity {
    ActivityDroneBinding binding;
    DroneRequestService requestService;
    DroneService droneService;
    DroneAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDroneBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        requestService = new DroneRequestService(DroneActivity.this);
        setListener();
        binding.btnSave.setEnabled(false);
        droneService = new DroneService(DroneActivity.this);
        loadList();
    }

    private void loadList() {
        int id = new LocalPref(DroneActivity.this).getIntItem("id");
        droneService.getDroneList(id, new DefaultBaseListener() {
            @Override
            public <T> void onSuccess(T any) {
                if (any instanceof List<?>) {
                    List<?> tmpList = (List<?>) any;
                    List<Drones> list = (List<Drones>) tmpList;
                    adapter = new DroneAdapter(DroneActivity.this, list, new DefaultEventListener() {
                        @Override
                        public void onClickListener() {

                        }

                        @Override
                        public void onClickListener(int position) {
                            Drones drones = list.get(position);
                            Intent intent = new Intent(DroneActivity.this, FullDroneActivity.class);
                            intent.putExtra("drone", new Gson().toJson(drones));
                            startActivity(intent);
                        }
                    });
                    binding.recycler.setLayoutManager(new LinearLayoutManager(DroneActivity.this));
                    binding.recycler.setAdapter(adapter);
                }
            }

            @Override
            public void onError(Error error) {

            }
        });
    }

    private void setListener() {
        binding.btnPing.setOnClickListener(v -> {
            String droneName = binding.editDroneName.getText().toString().trim();
            String ip = binding.editIP.getText().toString().trim();
            if (droneName.equals("") || ip.equals("")) {
                binding.btnSave.setEnabled(false);
                Toast.makeText(DroneActivity.this, "Please Don't Leave Empty Fields", Toast.LENGTH_SHORT).show();
            } else {
                LocalVolleyRequestBody body = new LocalVolleyRequestBody.LocalVolleyRequestBodyBuilder()
                        .setUrl(String.format("http://%s/health", ip))
                        .build();
                requestService.pingDrone(body, new LocalVolleyRequestListener() {
                    @Override
                    public void onSuccessJSON(JSONObject object) {
                        Toast.makeText(DroneActivity.this, "Drone was able to ping successfully, you can proceed saving it now ..", Toast.LENGTH_SHORT).show();
                        binding.btnSave.setEnabled(true);
                    }

                    @Override
                    public void onError(Error error) {
                        Toast.makeText(DroneActivity.this, "Failed To Ping Drone, Please Make Sure Drone Was Turned On And Near", Toast.LENGTH_SHORT).show();
                        binding.btnSave.setEnabled(false);
                    }
                });
            }
        });
        binding.btnSave.setOnClickListener(v -> {
            String droneName = binding.editDroneName.getText().toString().trim();
            String ip = binding.editIP.getText().toString().trim();
            if (droneName.equals("") || ip.equals("")) {
                binding.btnSave.setEnabled(false);
                Toast.makeText(DroneActivity.this, "Please Don't Leave Empty Fields", Toast.LENGTH_SHORT).show();
            } else {

                int id = new LocalPref(DroneActivity.this).getIntItem("id");
                Drones drones = new Drones.DroneBuilder()
                        .setDroneName(droneName)
                        .setUserID(id)
                        .setIp(ip)
                        .setStatus("active")
                        .build();
                droneService.insertDrone(drones, new DefaultBaseListener() {
                    @Override
                    public <T> void onSuccess(T any) {
                        Toast.makeText(DroneActivity.this, "Successfully Added Drone", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Error error) {
                        Toast.makeText(DroneActivity.this, "Failed To Add Drone, Please Try Again Later", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
