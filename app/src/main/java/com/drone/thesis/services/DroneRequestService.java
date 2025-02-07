package com.drone.thesis.services;

import android.content.Context;

import com.github.MakMoinee.library.interfaces.LocalVolleyRequestListener;
import com.github.MakMoinee.library.models.LocalVolleyRequestBody;
import com.github.MakMoinee.library.services.LocalVolleyRequest;

public class DroneRequestService extends LocalVolleyRequest {

    public DroneRequestService(Context mContext) {
        super(mContext);
    }

    public void pingDrone(LocalVolleyRequestBody body, LocalVolleyRequestListener listener) {
        this.sendJSONGetRequest(body, listener);
    }
}
