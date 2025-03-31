package com.drone.thesis.services;

import android.content.Context;
import android.util.Log;

import com.github.MakMoinee.library.interfaces.DefaultBaseListener;
import com.github.MakMoinee.library.interfaces.LocalVolleyRequestListener;
import com.github.MakMoinee.library.models.LocalVolleyRequestBody;
import com.github.MakMoinee.library.services.LocalVolleyRequest;

import org.json.JSONArray;
import org.json.JSONObject;

public class DroneRequestService extends LocalVolleyRequest {

    private final String controlString = "http://%s/control?pitch=%.2f&roll=%.2f&throttle=%.2f&yaw=%.2f";

    public DroneRequestService(Context mContext) {
        super(mContext);
    }

    public void pingDrone(LocalVolleyRequestBody body, LocalVolleyRequestListener listener) {
        this.sendTextPlainRequest(body, listener);
    }


    public void takeOff(String ip, LocalVolleyRequestListener listener) {
        LocalVolleyRequestBody body = new LocalVolleyRequestBody.LocalVolleyRequestBodyBuilder()
                .setUrl(String.format("http://%s/takeoff", ip))
                .build();
        this.sendTextPlainRequest(body, new LocalVolleyRequestListener() {
            @Override
            public void onSuccessString(String response) {
                listener.onSuccessString("success");
            }

            @Override
            public void onError(Error error) {
                listener.onError(error);
            }
        });
    }

    public void landing(String ip, LocalVolleyRequestListener listener) {
        LocalVolleyRequestBody body = new LocalVolleyRequestBody.LocalVolleyRequestBodyBuilder()
                .setUrl(String.format("http://%s/landing", ip))
                .build();
        this.sendTextPlainRequest(body, new LocalVolleyRequestListener() {
            @Override
            public void onSuccessString(String response) {
                listener.onSuccessString("success");
            }

            @Override
            public void onError(Error error) {
                listener.onError(error);
            }
        });
    }


    public void disarm(String ip, LocalVolleyRequestListener listener) {
        LocalVolleyRequestBody body = new LocalVolleyRequestBody.LocalVolleyRequestBodyBuilder()
                .setUrl(String.format("http://%s/disarm", ip))
                .build();
        this.sendTextPlainRequest(body, listener);
    }

    public void arm(String ip, LocalVolleyRequestListener listener) {
        LocalVolleyRequestBody body = new LocalVolleyRequestBody.LocalVolleyRequestBodyBuilder()
                .setUrl(String.format("http://%s/arm", ip))
                .build();
        this.sendTextPlainRequest(body, listener);
    }

    public void fetchThermal(String ip, DefaultBaseListener listener) {
        LocalVolleyRequestBody body = new LocalVolleyRequestBody.LocalVolleyRequestBodyBuilder()
                .setUrl(String.format("http://%s/thermal", ip))
                .build();
        this.sendJSONArrayGetRequest(body, new LocalVolleyRequestListener() {
            @Override
            public void onSuccessJSONArray(JSONArray response) {
                try {
                    int rows = response.length();
                    int cols = response.getJSONArray(0).length();
                    float[][] thermalData = new float[rows][cols];

                    for (int i = 0; i < rows; i++) {
                        JSONArray rowArray = response.getJSONArray(i);
                        for (int j = 0; j < cols; j++) {
                            thermalData[i][j] = (float) rowArray.getDouble(j);
                        }
                    }

                    // Notify listener
                    listener.onSuccess(thermalData);

                } catch (Exception e) {
                    Log.e("fetchThermal", "Error parsing thermal data", e);
                    listener.onError(new Error(e.getLocalizedMessage()));
                }
            }

            @Override
            public void onError(Error error) {

                listener.onError(error);
            }
        });
    }

    public void sendCommand(String ip, float pitch, float roll, float throttle, float yaw, LocalVolleyRequestListener listener) {
        String formattedUrl = String.format(controlString, ip, pitch, roll, throttle, yaw);
        LocalVolleyRequestBody body = new LocalVolleyRequestBody.LocalVolleyRequestBodyBuilder()
                .setUrl(formattedUrl)
                .build();
        this.sendTextPlainRequest(body, listener);

    }

}
