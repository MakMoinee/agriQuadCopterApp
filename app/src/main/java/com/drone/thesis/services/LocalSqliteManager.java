package com.drone.thesis.services;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.drone.thesis.commons.LocalDefaults;
import com.drone.thesis.repository.LocalSQL;
import com.github.MakMoinee.library.services.MSqlite;

public class LocalSqliteManager {
    private static LocalSqliteManager instance;
    private static LocalSQL sqliteService;
    private boolean isBound = false;

    private LocalSqliteManager() {
    }


    public static synchronized LocalSqliteManager getInstance(Context context) {
        if (instance == null) {
            instance = new LocalSqliteManager();
            instance.bindService(context);
        }
        return instance;
    }

    private void bindService(Context context) {
        Intent intent = new Intent(context, LocalSQL.class);
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocalSQL.LocalBinder binder = (LocalSQL.LocalBinder) service;
            sqliteService = (LocalSQL) binder.getService();
            isBound = true;
            LocalDefaults.isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
            LocalDefaults.isBound = false;
        }
    };

    public MSqlite getSQLiteService() {
        if (isBound) {
            return sqliteService;
        } else {
            return null;
        }
    }
}
