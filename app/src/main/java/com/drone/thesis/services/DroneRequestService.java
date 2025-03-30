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

    public DroneRequestService(Context mContext) {
        super(mContext);
    }

    public void pingDrone(LocalVolleyRequestBody body, LocalVolleyRequestListener listener) {
        this.sendTextPlainRequest(body, listener);
    }


    public void takeOff(String ip, LocalVolleyRequestListener listener) {
        LocalVolleyRequestBody body = new LocalVolleyRequestBody.LocalVolleyRequestBodyBuilder()
                .setUrl(String.format("http://%s/takeOff", ip))
                .build();
        this.sendJSONGetRequest(body, new LocalVolleyRequestListener() {
            @Override
            public void onSuccessJSON(JSONObject object) {
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
                .setUrl(String.format("http://%s/land", ip))
                .build();
        this.sendJSONGetRequest(body, new LocalVolleyRequestListener() {
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

}
