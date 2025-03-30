package com.drone.thesis.services;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.drone.thesis.models.Drones;
import com.drone.thesis.models.Users;
import com.drone.thesis.repository.LocalSqlite;
import com.github.MakMoinee.library.common.MapForm;
import com.github.MakMoinee.library.interfaces.DefaultBaseListener;

import java.util.ArrayList;
import java.util.List;

public class DroneService {
    private LocalSqlite sqlite;
    private final String TABLE_DRONES = "drones";

    public DroneService(Context mContext) {
        sqlite = LocalSqlite.getInstance(mContext);
    }

    public void insert(Drones drones, DefaultBaseListener listener) {
        SQLiteDatabase db = sqlite.getWritableDatabase();
        ContentValues values = MapForm.toContentValues(drones);
        values.remove("id");
        try {
            long count = db.insert(TABLE_DRONES, null, values);
            if (count != -1) {
                listener.onSuccess("success add");
            } else {
                listener.onError(new Error("failed to add error"));
            }
        } catch (Exception e) {
            Log.e("error_insert", e.getLocalizedMessage());
            listener.onError(new Error(e.getMessage()));
        } finally {
            db.close();
        }
    }

    @SuppressLint("Range")
    public void insertDrone(Drones drones, DefaultBaseListener listener) {
        this.checkDrone(drones, new DefaultBaseListener() {
            @Override
            public <T> void onSuccess(T any) {
                if (any instanceof Drones) {
                    Drones output = (Drones) any;
                    if (output != null) {
                        listener.onError(new Error("drone exists"));
                    }
                }
            }

            @Override
            public void onError(Error error) {
                insert(drones, new DefaultBaseListener() {
                    @Override
                    public <T> void onSuccess(T any) {
                        listener.onSuccess("Successfully Inserted Drone");
                    }

                    @Override
                    public void onError(Error error) {
                        listener.onError(new Error("failed to delete"));
                    }
                });
            }
        });
    }

    @SuppressLint("Range")
    public void checkDrone(Drones drones, DefaultBaseListener listener) {
        if (drones != null) {
            SQLiteDatabase db = sqlite.getWritableDatabase();
            String[] columns = {"id", "userID", "droneName", "ip", "status"};
            String selection = "droneName=?,userID=?";
            String[] selectionArgs = {drones.getDroneName(), Integer.toString(drones.getUserID())};
            Cursor cursor = null;
            Drones validDrone = null;
            try {
                cursor = db.query(TABLE_DRONES, columns, selection, selectionArgs, null, null, null);

                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        int id = cursor.getInt(cursor.getColumnIndex("id"));
                        int userID = cursor.getInt(cursor.getColumnIndex("userID"));
                        String droneName = cursor.getString(cursor.getColumnIndex("droneName"));
                        String ip = cursor.getString(cursor.getColumnIndex("ip"));
                        String status = cursor.getString(cursor.getColumnIndex("status"));
                        Log.e("id >>>>", Integer.toString(id));
                        validDrone = new Drones.DroneBuilder()
                                .setId(id)
                                .setUserID(userID)
                                .setDroneName(droneName)
                                .setIp(ip)
                                .setStatus(status)
                                .build();
                        break;

                    } while (cursor.moveToNext());
                }


            } catch (Exception e) {
                Log.e("drone_service_err", e.getLocalizedMessage());
            } finally {
                if (validDrone != null) {
                    listener.onSuccess(validDrone);
                } else {
                    listener.onError(new Error("empty drones"));
                }
            }

        } else {
            listener.onError(new Error("empty drones"));
        }
    }

    @SuppressLint("Range")
    public void getDroneList(int uid, DefaultBaseListener listener) {
        if (uid != 0) {
            SQLiteDatabase db = sqlite.getReadableDatabase();
            String[] columns = {"id", "userID", "droneName", "ip", "status"};
            String selection = "userID=?";
            String[] selectionArgs = {Integer.toString(uid)};

            Cursor cursor = null;
            List<Drones> droneList = new ArrayList<>();

            try {
                cursor = db.query(TABLE_DRONES, columns, selection, selectionArgs, null, null, null);

                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        int id = cursor.getInt(cursor.getColumnIndex("id"));
                        int userID = cursor.getInt(cursor.getColumnIndex("userID"));
                        String droneName = cursor.getString(cursor.getColumnIndex("droneName"));
                        String ip = cursor.getString(cursor.getColumnIndex("ip"));
                        String status = cursor.getString(cursor.getColumnIndex("status"));

                        Drones drone = new Drones.DroneBuilder()
                                .setId(id)
                                .setUserID(userID)
                                .setDroneName(droneName)
                                .setIp(ip)
                                .setStatus(status)
                                .build();

                        droneList.add(drone);
                    } while (cursor.moveToNext());
                }

                if (!droneList.isEmpty()) {
                    listener.onSuccess(droneList);
                } else {
                    listener.onError(new Error("No drones found"));
                }

            } catch (Exception e) {
                Log.e("getDroneList_error", e.getLocalizedMessage());
                listener.onError(new Error(e.getMessage()));
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
                db.close();
            }
        } else {
            listener.onError(new Error("Invalid drone filter"));
        }
    }

    public void deleteDrone(Drones drones, DefaultBaseListener listener) {
        if (drones != null) {
            SQLiteDatabase db = sqlite.getWritableDatabase();
            try {
                int affectedRows = db.delete(TABLE_DRONES, "id = ?", new String[]{String.valueOf(drones.getId())});
                if (affectedRows > 0) {
                    listener.onSuccess("Drone successfully deleted");
                } else {
                    listener.onError(new Error("Failed to delete drone or drone not found"));
                }
            } catch (Exception e) {
                Log.e("deleteDrone_error", e.getLocalizedMessage());
                listener.onError(new Error(e.getMessage()));
            } finally {
                db.close();
            }
        } else {
            listener.onError(new Error("Drone object is null"));
        }
    }

}
