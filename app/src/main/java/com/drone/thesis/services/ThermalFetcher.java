package com.drone.thesis.services;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.github.MakMoinee.library.interfaces.DefaultBaseListener;

import java.net.URL;

public class ThermalFetcher {
    private String picoIp; // Store the Pico W's IP
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final int REFRESH_INTERVAL = 2000; // Refresh every 2 seconds
    private ThermalDataListener listener;

    Context mContext;

    public interface ThermalDataListener {
        void onThermalDataReceived(float[][] thermalData);
    }

    // Constructor that accepts IP
    public ThermalFetcher(Context ctx, String picoIp, ThermalDataListener listener) {
        this.picoIp = picoIp;
        this.listener = listener;
        this.mContext = ctx;
    }

    // Method to update the IP dynamically
    public void updateIp(String newIp) {
        this.picoIp = newIp;
    }

    public void startFetching() {
        handler.post(fetchRunnable);
    }

    public void stopFetching() {
        handler.removeCallbacks(fetchRunnable);
    }

    private final Runnable fetchRunnable = new Runnable() {
        @Override
        public void run() {
            new Thread(() -> {
                try {
                    new DroneRequestService(mContext).fetchThermal(picoIp, new DefaultBaseListener() {
                        @Override
                        public <T> void onSuccess(T any) {
                            if (any instanceof float[][] thermalData) {

                                // Log received data
                                Log.d("ThermalFetcher", "Received thermal data: " + thermalData.length + "x" + thermalData[0].length);

                                // Notify listener on the UI thread
                                if (listener != null) {
                                    handler.post(() -> listener.onThermalDataReceived(thermalData));
                                }
                            }
                        }

                        @Override
                        public void onError(Error error) {

                        }
                    });

                } catch (Exception e) {
                    Log.e("ThermalFetcher", "Error fetching thermal data", e);
                }

                // Schedule next fetch
                handler.postDelayed(this, REFRESH_INTERVAL);
            }).start();
        }
    };
}
