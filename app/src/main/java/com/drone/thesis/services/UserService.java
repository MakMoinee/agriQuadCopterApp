package com.drone.thesis.services;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.drone.thesis.commons.LocalDefaults;
import com.drone.thesis.models.Users;
import com.drone.thesis.repository.LocalSQL;
import com.github.MakMoinee.library.common.MapForm;
import com.github.MakMoinee.library.interfaces.DefaultBaseListener;
import com.github.MakMoinee.library.services.MSqlite;

public class UserService {

    private MSqlite sql;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocalSQL.LocalBinder binder = (LocalSQL.LocalBinder) service;
            sql = binder.getService();
            LocalDefaults.isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LocalDefaults.isBound = false;
        }
    };

    public UserService(MSqlite sm) {
        if (sm == null) {
            Log.e("sql_err", "null");

            this.sql = new LocalSQL();
        } else {
            this.sql = sm;
            LocalDefaults.isBound = true;
        }
    }

    public UserService() {
    }

    public void insertUser(Users users, DefaultBaseListener listener) {
        ContentValues values = MapForm.toContentValues(users);
        if (values != null) {
            long dataCount = sql.insertRecord("users", values);
            if (dataCount > 0) {
                listener.onSuccess("successfully added user");
            } else {
                listener.onError(new Error("failed to insert user"));
            }
        } else {
            listener.onError(new Error("empty values"));
        }

    }
}
