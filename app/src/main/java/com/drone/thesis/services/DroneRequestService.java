package com.drone.thesis.services;

import android.content.Context;

import com.github.MakMoinee.library.interfaces.LocalVolleyRequestListener;
import com.github.MakMoinee.library.models.LocalVolleyRequestBody;
import com.github.MakMoinee.library.services.LocalVolleyRequest;

import org.json.JSONObject;

public class DroneRequestService extends LocalVolleyRequest {

    public DroneRequestService(Context mContext) {
        super(mContext);
    }

    public void pingDrone(LocalVolleyRequestBody body, LocalVolleyRequestListener listener) {
        this.sendJSONGetRequest(body, listener);
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
}
